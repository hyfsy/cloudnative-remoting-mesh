package com.hyf.cloudnative.remoting.mesh.proxy.http.interceptor;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import com.hyf.cloudnative.remoting.mesh.proxy.http.Request;
import com.hyf.cloudnative.remoting.mesh.proxy.http.RequestInterceptor;
import com.hyf.cloudnative.remoting.mesh.proxy.http.WebUtils;
import org.springframework.aop.support.AopUtils;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;
import java.util.*;

public class RequestMappingAnnotationParser implements RequestInterceptor {

    @Override
    public void process(Request request, Object proxy, Method method, Object[] args, InvocationContext<RestTemplate> context) {
        processRequestMappingAnnotation(request, proxy, method, context);
    }

    private void processRequestMappingAnnotation(Request request, Object proxy, Method method, InvocationContext<RestTemplate> context) {
        Class<?> targetClass = AopUtils.getTargetClass(proxy);

        RequestMapping classAnnotation = AnnotatedElementUtils.findMergedAnnotation(targetClass, RequestMapping.class);
        RequestMapping methodAnnotation = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);

        setUrl(request, classAnnotation, methodAnnotation, context);
        setRequestMethod(request, classAnnotation, methodAnnotation);
        setParams(request, classAnnotation, methodAnnotation);
        setHeaders(request, classAnnotation, methodAnnotation);
    }

    private void setUrl(Request request, RequestMapping classAnnotation, RequestMapping methodAnnotation, InvocationContext<RestTemplate> context) {

        List<String> paths = new ArrayList<>();

        if (context.getAddr() == null) {
            throw new IllegalStateException("Request prefix is null");
        }
        paths.add(context.getAddr());

        if (classAnnotation != null) {
            String[] value = classAnnotation.value();
            paths.addAll(Arrays.asList(value));
        }
        if (methodAnnotation != null) {
            String[] value = methodAnnotation.value();
            paths.addAll(Arrays.asList(value));
        }

        request.setUrl(WebUtils.concat(paths));
    }

    private void setRequestMethod(Request request, RequestMapping classAnnotation, RequestMapping methodAnnotation) {
        HttpMethod httpMethod;
        RequestMethod[] method = {};

        if (methodAnnotation != null && methodAnnotation.method().length > 0) {
            method = methodAnnotation.method();
        } else if (classAnnotation != null) {
            method = classAnnotation.method();
        }

        if (method.length == 0) {
            httpMethod = HttpMethod.GET; // default get
        } else {
            httpMethod = HttpMethod.valueOf(method[0].name());
        }
        request.setHttpMethod(httpMethod);
    }

    private void setParams(Request request, RequestMapping classAnnotation, RequestMapping methodAnnotation) {
        Map<String, List<String>> params = new HashMap<>();

        if (methodAnnotation != null && methodAnnotation.params().length > 0) {
            params.putAll(parseList(methodAnnotation.params()));
        } else if (classAnnotation != null) {
            params.putAll(parseList(classAnnotation.params()));
        }

        request.setParams(params);
    }

    private void setHeaders(Request request, RequestMapping classAnnotation, RequestMapping methodAnnotation) {
        Map<String, List<String>> headers = new HashMap<>();

        if (methodAnnotation != null && methodAnnotation.produces().length > 0) {
            headers.put("Accept", Arrays.asList(methodAnnotation.produces()));
        } else if (classAnnotation != null && classAnnotation.produces().length > 0) {
            headers.put("Accept", Arrays.asList(classAnnotation.produces()));
        }

        if (methodAnnotation != null && methodAnnotation.consumes().length > 0) {
            headers.put("Content-Type", Arrays.asList(methodAnnotation.consumes()));
        } else if (classAnnotation != null && classAnnotation.consumes().length > 0) {
            headers.put("Content-Type", Arrays.asList(classAnnotation.consumes()));
        }

        if (methodAnnotation != null && methodAnnotation.headers().length > 0) {
            headers.putAll(parseList(methodAnnotation.headers()));
        } else if (classAnnotation != null) {
            headers.putAll(parseList(classAnnotation.headers()));
        }

        request.setHeaders(headers);
    }

    private Map<String, List<String>> parseList(String[] params) {
        Map<String, List<String>> map = new HashMap<>();
        for (String header : params) {
            int index = header.indexOf('=');
            if (!header.contains("!=") && index >= 0) {
                map.put(header.substring(0, index), Arrays.asList(header.substring(index + 1).trim()));
            }
        }
        return map;
    }
}
