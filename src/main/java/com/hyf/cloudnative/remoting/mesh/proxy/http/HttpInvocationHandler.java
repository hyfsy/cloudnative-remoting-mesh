package com.hyf.cloudnative.remoting.mesh.proxy.http;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import com.hyf.cloudnative.remoting.mesh.proxy.AbstractRemotingInvocationHandler;
import com.hyf.cloudnative.remoting.mesh.utils.ApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.util.MultiValueMapAdapter;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

public class HttpInvocationHandler extends AbstractRemotingInvocationHandler<RestTemplate> {

    private static final Logger log = LoggerFactory.getLogger(HttpInvocationHandler.class);

    public HttpInvocationHandler(InvocationContext<RestTemplate> invocationContext) {
        super(invocationContext);
        RequestAnnotationMetadataUtils.parse(invocationContext.getType());
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
        Request request = createRequest(proxy, method, args);
        return invokeAndReturn(request, method);
    }

    private Request createRequest(Object proxy, Method method, Object[] args) {
        Request request = new Request();
        ApplicationUtils.getOrderedList(RequestInterceptor.class).forEach(i -> i.process(request, proxy, method, args, getContext()));
        return request;
    }

    private Object invokeAndReturn(Request request, Method method) {
        RestTemplate restTemplate = getContext().getClient();
        if (restTemplate == null) {
            throw new IllegalStateException("RestTemplate is null");
        }

        // url replace
        String url = request.getUrl();
        Map<String, List<String>> params = request.getParams();
        if (!params.isEmpty()) {
            StringBuilder paramBuilder = new StringBuilder();
            params.forEach((k, v) -> {
                for (String s : v) {
                    if (paramBuilder.length() != 0) {
                        paramBuilder.append("&");
                    }
                    paramBuilder.append(k).append("=").append(s);
                }
            });
            paramBuilder.insert(0, url);
            paramBuilder.insert(url.length(), "?");
            url = paramBuilder.toString();
        }

        if (log.isDebugEnabled()) {
            log.debug("Request address: {}, message: {}", url, request);
        }

        // execute
        HttpEntity<?> httpEntity = new HttpEntity<>(request.getBody(), new MultiValueMapAdapter<>(request.getHeaders()));
        return restTemplate.execute(url, request.getHttpMethod(),
                restTemplate.httpEntityCallback(httpEntity, method.getReturnType()),
                new HttpMessageConverterExtractor<>(method.getReturnType(), restTemplate.getMessageConverters()), params /* uriVariables */);
    }

    // RestTemplate has been done this action
    @Deprecated
    private String rawurlencode(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8")
                    .replace("+", "%20")
                    .replace("*", "%A")
                    .replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            return url;
        }
    }
}
