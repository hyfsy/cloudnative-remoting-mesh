package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client.GrpcClient;

import java.lang.reflect.Method;

/**
 * grpc message interceptor, processes before the message will be send, user can
 * extends to change the message.
 */
public interface MessageInterceptor {

    /**
     * intercept the message before it will be send.
     *
     * @param message grpc message object
     * @param proxy   grpc client proxy instance
     * @param method  invoked client method
     * @param args    invoked client args
     * @param context client context
     */
    void process(Message message, Object proxy, Method method, Object[] args, InvocationContext<GrpcClient> context);

}
