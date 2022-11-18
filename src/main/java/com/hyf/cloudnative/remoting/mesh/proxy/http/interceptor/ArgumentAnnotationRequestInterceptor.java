package com.hyf.cloudnative.remoting.mesh.proxy.http.interceptor;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import com.hyf.cloudnative.remoting.mesh.proxy.http.Request;
import com.hyf.cloudnative.remoting.mesh.proxy.http.RequestInterceptor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class ArgumentAnnotationRequestInterceptor implements RequestInterceptor {

    private static final Class<?>[] httpClasses = {PathVariable.class, RequestParam.class, RequestHeader.class}; // not support RequestPart.class, MatrixVariable.class

    @Override
    public void process(Request request, Object proxy, Method method, Object[] args, InvocationContext<RestTemplate> context) {
        checkMetadata(method);
        processArgumentAnnotation(request, method, args);
    }

    private void checkMetadata(Method method) {
        Type[] parameterTypes = method.getGenericParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            if (containsAnnotation(parameterAnnotation, RequestParam.class)) {
                if (parameterTypes[i] instanceof ParameterizedType) {
                    ParameterizedType parameterType = (ParameterizedType) parameterTypes[i];
                    if (parameterType.getRawType() == Map.class) {
                        if (parameterType.getActualTypeArguments()[0] != String.class) {
                            throw new IllegalArgumentException("Map key must be a String: " + parameterType.getActualTypeArguments()[0].getTypeName());
                        }
                    }
                }
            }
        }
    }

    private void processArgumentAnnotation(Request request, Method method, Object[] args) {

        Annotation[][] parameterAnnotations = method.getParameterAnnotations();

        Object body = null;

//        Map<String, String> pathVariablesValueMap = new HashMap<>();

        for (int i = 0; i < parameterAnnotations.length; i++) {
            Annotation[] parameterAnnotation = parameterAnnotations[i];
            // body value set
            if (body == null && !hasHttpAnnotation(parameterAnnotation)) { //  || containsAnnotation(parameterAnnotation, RequestBody.class)
                body = args[i];
            }

            if (args[i] == null) {
                continue;
            }

            // parse http annotation
            for (Annotation annotation : parameterAnnotation) {
                if (annotation.annotationType() == PathVariable.class) {
                    PathVariable pathVariableAnnotation = PathVariable.class.cast(annotation);
                    String value = pathVariableAnnotation.value();
                    Assert.hasText(value, "@PathVariable value() must has text");
                    request.setUrl(request.getUrl().replaceFirst("\\{" + value + "}", String.valueOf(args[i])));
                } else if (annotation.annotationType() == RequestHeader.class) {
                    RequestHeader requestHeaderAnnotation = RequestHeader.class.cast(annotation);
                    String value = requestHeaderAnnotation.value();
                    Assert.hasText(value, "@RequestHeader value() must has text");
                    request.getHeaders().put(value, Arrays.asList(String.valueOf(args[i])));
                } else if (annotation.annotationType() == RequestParam.class) {
                    RequestParam requestParamAnnotation = RequestParam.class.cast(annotation);
                    String key = requestParamAnnotation.value();
                    Assert.hasText(key, "@RequestParam value() must has text");

                    List<String> params = new ArrayList<>();

                    if (args[i] instanceof Iterable) {
                        for (Object o : ((Iterable<?>) args[i])) {
                            params.add(String.valueOf(o));
                        }
                        request.getParams().put(key, params);
                    } else if (args[i] instanceof Object[]) {
                        for (Object o : ((Object[]) args[i])) {
                            params.add(String.valueOf(o));
                        }
                        request.getParams().put(key, params);
                    } else if (args[i] instanceof Map) {

                        for (Map.Entry<?, ?> entry : ((Map<?, ?>) args[i]).entrySet()) {
                            Object k = entry.getKey();
                            Object v = entry.getValue();

                            List<String> mapParams = new ArrayList<>();

                            String mapKey = String.valueOf(k);

                            if (v instanceof Iterable) {
                                for (Object o : ((Iterable<?>) v)) {
                                    mapParams.add(String.valueOf(o));
                                }
                            } else if (v instanceof Object[]) {
                                for (Object o : ((Object[]) v)) {
                                    mapParams.add(String.valueOf(o));
                                }
                            } else {
                                mapParams.add(String.valueOf(v));
                            }

                            request.getParams().put(mapKey, mapParams);
                        }
                    } else {
                        params.add(String.valueOf(args[i]));
                        request.getParams().put(key, params);
                    }
                }
            }
        }

        request.setBody(body);
    }

    private boolean containsAnnotation(Annotation[] annotations, Class<?> clazz) {
        for (Annotation annotation : annotations) {
            if (clazz == annotation.annotationType()) {
                return true;
            }
        }
        return false;
    }

    private boolean hasHttpAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            for (Class<?> httpClass : httpClasses) {
                if (httpClass == annotation.annotationType()) {
                    return true;
                }
            }
        }
        return false;
    }
}
