package com.hyf.cloudnative.remoting.mesh.proxy.http;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import com.hyf.cloudnative.remoting.mesh.proxy.AbstractRemotingInvocationHandler;
import com.hyf.cloudnative.remoting.mesh.utils.ApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.*;

/**
 * TODO
 *   annotation parse cache
 */
public class HttpInvocationHandler extends AbstractRemotingInvocationHandler<RestTemplate> {

    private static final Logger log = LoggerFactory.getLogger(HttpInvocationHandler.class);

    public HttpInvocationHandler(InvocationContext<RestTemplate> invocationContext) {
        super(invocationContext);
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
        return restTemplate.execute(url, request.getHttpMethod(),
                new HeaderAndBodySetCallback(restTemplate.httpEntityCallback(request.getBody(), method.getReturnType())) {

                    @Override
                    protected Map<String, List<String>> getHeaders() {
                        return request.getHeaders();
                    }
                },
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

    private abstract static class HeaderAndBodySetCallback implements RequestCallback {

        private final RequestCallback delegate;

        public HeaderAndBodySetCallback(RequestCallback delegate) {
            this.delegate = delegate;
        }

        @Override
        public void doWithRequest(ClientHttpRequest request) throws IOException {
            request.getHeaders().putAll(getHeaders()); // header set
            delegate.doWithRequest(request); // body set
        }

        protected abstract Map<String, List<String>> getHeaders();
    }
}
