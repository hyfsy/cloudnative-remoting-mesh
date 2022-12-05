package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import com.hyf.cloudnative.remoting.mesh.RequestWay;
import com.hyf.cloudnative.remoting.mesh.proxy.AbstractFallbackProxyProvider;
import com.hyf.cloudnative.remoting.mesh.proxy.ClientConfig;
import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client.GrpcClient;
import org.springframework.beans.factory.BeanFactory;

import java.lang.reflect.InvocationHandler;

public class GrpcProxyProvider extends AbstractFallbackProxyProvider {

    @Override
    public String requestWay() {
        return RequestWay.GRPC;
    }

    @Override
    public InvocationHandler createInvocationHandler(BeanFactory beanFactory, ClientConfig clientConfig) {
        Class<?> type = clientConfig.getType();
        String host = clientConfig.generateServiceHost(beanFactory);
        int port = clientConfig.getPort();

        InvocationMetadataRegistry.registryClass(type);

        boolean tlsEnable = clientConfig.isTlsEnable();
        // http://www.caotama.com/1899347.html
        GrpcClient client = beanFactory.getBean(GrpcClient.class); // TODO tlsEnable to choose different client

        InvocationContext<GrpcClient> invocationContext = new InvocationContext<>(type, client, host + ":" + port);
        return new GrpcInvocationHandler(invocationContext);
    }
}
