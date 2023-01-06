package com.hyf.cloudnative.remoting.mesh.proxy.http.interceptor;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import com.hyf.cloudnative.remoting.mesh.proxy.http.Request;
import com.hyf.cloudnative.remoting.mesh.proxy.http.RequestInterceptor;
import com.hyf.cloudnative.remoting.mesh.proxy.http.file.MultipartFileAdapter;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Priority;
import javax.servlet.http.Part;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Deprecated
@Priority(-400)
public class ArgumentAnnotationRequestInterceptor implements RequestInterceptor {

    private static final Class<?>[] httpClasses = {PathVariable.class, RequestParam.class, RequestHeader.class, CookieValue.class, RequestPart.class}; // not support RequestPart.class, MatrixVariable.class

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
            // TODO 检查名称必须存在
            if (containsAnnotation(parameterAnnotation, RequestPart.class)) {
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
        boolean hasMultipart = false;
        Object multipart = null;
        Map<String, List<MultipartFile>> multipartFilesMap = null;

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
                    request.setUrl(request.getUrl().replaceFirst("\\{" + value + "}", convert(args[i])));
                } else if (annotation.annotationType() == RequestHeader.class) {
                    RequestHeader requestHeaderAnnotation = RequestHeader.class.cast(annotation);
                    String value = requestHeaderAnnotation.value();
                    Assert.hasText(value, "@RequestHeader value() must has text");
                    request.getHeaders().put(value, Arrays.asList(convert(args[i])));
                } else if (annotation.annotationType() == RequestParam.class) {
                    RequestParam requestParamAnnotation = RequestParam.class.cast(annotation);
                    String key = requestParamAnnotation.value();
                    Assert.hasText(key, "@RequestParam value() must has text");

                    if (args[i] instanceof Map) {
                        for (Map.Entry<?, ?> entry : ((Map<?, ?>) args[i]).entrySet()) {
                            Object k = entry.getKey();
                            Object v = entry.getValue();

                            List<String> mapParams = new ArrayList<>();
                            String mapKey = convert(k);
                            iterateObject(v, o -> mapParams.add(convert(o)));
                            request.getParams().put(mapKey, mapParams);
                        }
                    }
                    else {
                        List<String> params = new ArrayList<>();
                        iterateObject(args[i], o -> params.add(convert(o)));
                        request.getParams().put(key, params);
                    }
                } else if (annotation.annotationType() == CookieValue.class) {
                    CookieValue cookieValueAnnotation = CookieValue.class.cast(annotation);
                    String cookieName = cookieValueAnnotation.value();
                    request.getHeaders().putIfAbsent(HttpHeaders.COOKIE, new ArrayList<>());
                    request.getHeaders().get(HttpHeaders.COOKIE).add(cookieName + "=" + convert(args[i]));
                } else if (annotation.annotationType() == RequestPart.class) {
                    hasMultipart = true;
                    RequestPart requestPartAnnotation = RequestPart.class.cast(annotation);
                    String partName = requestPartAnnotation.value();
                    Object arg = args[i];
                    if (multipartFilesMap == null) {
                        multipartFilesMap = new HashMap<>();
                    }
                    addMultipartFiles(multipartFilesMap, partName, arg);
                }
            }
        }

        if (hasMultipart && multipartFilesMap != null) {
            // origin
            // multipart = FormDataParser.parse(partName, arg);

            // SpringMVC
            // FormHttpMessageConverter
            Map<String, List<Resource>> tempMultipartFilesMap = new HashMap<>();
            for (String name : multipartFilesMap.keySet()) {
                tempMultipartFilesMap.put(name, multipartFilesMap.get(name).stream().map(MultipartFile::getResource).collect(Collectors.toList()));
            }
            multipart = new MultiValueMapAdapter<>(tempMultipartFilesMap);
            request.setBody(multipart);
        }
        else {
            request.setBody(body);
        }
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

    private String convert(Object o) {
        return String.valueOf(o);
    }

    private void addMultipartFiles(Map<String, List<MultipartFile>> multipartFilesMap, String partName, Object arg) {
        multipartFilesMap.putIfAbsent(partName, new ArrayList<>());

        iterateObject(arg, o -> {
            if (o instanceof Part) {
                multipartFilesMap.get(partName).add(new MultipartFileAdapter((Part) o));
            }
            else if (o instanceof MultipartFile) {
                multipartFilesMap.get(partName).add((MultipartFile) o);
            }
        });
    }

    private void iterateObject(Object o, Consumer<Object> iteratorConsumer) {
        if (o instanceof Iterable) {
            for (Object item : ((Iterable<?>) o)) {
                iteratorConsumer.accept(item);
            }
        } else if (o instanceof Object[]) {
            for (Object item : ((Object[]) o)) {
                iteratorConsumer.accept(item);
            }
        } else {
            iteratorConsumer.accept(o);
        }
    }
}
