package com.hyf.cloudnative.remoting.mesh.proxy.http;

import com.hyf.cloudnative.remoting.mesh.RequestWay;
import com.hyf.cloudnative.remoting.mesh.proxy.AbstractFallbackProxyProvider;
import com.hyf.cloudnative.remoting.mesh.proxy.ClientConfig;
import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;

public class HttpProxyProvider extends AbstractFallbackProxyProvider {

    private static final RestTemplate restTemplate = new RestTemplateBuilder().build();

    @Override
    public RequestWay requestWay() {
        return RequestWay.HTTP;
    }

    @Override
    protected InvocationHandler createInvocationHandler(BeanFactory beanFactory, ClientConfig clientConfig) {
        Class<?> type = clientConfig.getType();
        InvocationContext<RestTemplate> invocationContext = new InvocationContext<>(type, getRestTemplate(beanFactory), getHttpUrlPrefix(beanFactory, clientConfig));
        return new HttpInvocationHandler(invocationContext);
    }

    private String getHttpUrlPrefix(BeanFactory beanFactory, ClientConfig clientConfig) {
        String prefix = clientConfig.isTlsEnable() ? "https" : "http";
        int port = clientConfig.getPort() > 0 ? clientConfig.getPort() : (clientConfig.isTlsEnable() ? 443 : 80);
        return prefix + "://" + clientConfig.generateServiceHost(beanFactory) + ":" + port;
    }

    private RestTemplate getRestTemplate(BeanFactory beanFactory) {
        return restTemplate;
    }
}
