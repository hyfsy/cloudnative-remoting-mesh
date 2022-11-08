package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client.GrpcClient;

import java.lang.reflect.Method;

public interface MessageInterceptor {

    void process(Message message, Object proxy, Method method, Object[] args, InvocationContext<GrpcClient> context);

}
