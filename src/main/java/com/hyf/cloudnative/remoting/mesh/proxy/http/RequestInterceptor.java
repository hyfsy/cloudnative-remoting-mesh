package com.hyf.cloudnative.remoting.mesh.proxy.http;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Method;

/**
 * http message interceptor, processes before the message will be send, user can
 * extends to change the message.
 */
public interface RequestInterceptor {

    /**
     * intercept the message before it will be send.
     *
     * @param request http message object
     * @param proxy   http client proxy instance
     * @param method  invoked client method
     * @param args    invoked client args
     * @param context client context
     */
    void process(Request request, Object proxy, Method method, Object[] args, InvocationContext<RestTemplate> context);

}
