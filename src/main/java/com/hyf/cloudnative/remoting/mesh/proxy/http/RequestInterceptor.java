package com.hyf.cloudnative.remoting.mesh.proxy.http;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;

public interface RequestInterceptor {

    void process(Request request, Object proxy, Method method, Object[] args, InvocationContext<RestTemplate> context);

}
