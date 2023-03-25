package com.hyf.cloudnative.remoting.mesh.proxy.http;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Priority;
import java.lang.reflect.Method;

@Priority(-500)
public class SpringMVCAnnotationRequestInterceptor implements RequestInterceptor {

    @Override
    public void process(Request request, Object proxy, Method method, Object[] args, InvocationContext<RestTemplate> context) {
        setContextAddr(request, context);
        RequestAnnotationMetadataUtils.apply(request, method, args);
    }

    private void setContextAddr(Request request, InvocationContext<RestTemplate> context) {
        String addr = context.getAddr();

        if (addr == null) {
            throw new IllegalStateException("Request prefix is null");
        }

        request.setUrl(addr);
    }
}
