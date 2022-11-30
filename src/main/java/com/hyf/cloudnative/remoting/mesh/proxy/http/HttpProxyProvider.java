package com.hyf.cloudnative.remoting.mesh.proxy.http;

import com.hyf.cloudnative.remoting.mesh.RequestWay;
import com.hyf.cloudnative.remoting.mesh.proxy.AbstractFallbackProxyProvider;
import com.hyf.cloudnative.remoting.mesh.proxy.ClientConfig;
import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.InvocationHandler;

public class HttpProxyProvider extends AbstractFallbackProxyProvider {

    private static final RestTemplate DEFAULT_REST_TEMPLATE = new RestTemplateBuilder().build();

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

    protected RestTemplate getRestTemplate(BeanFactory beanFactory) {
        RestTemplate restTemplate;
        try {
            restTemplate = beanFactory.getBean(RestTemplate.class);
        } catch (NoSuchBeanDefinitionException ignored) {
            restTemplate = DEFAULT_REST_TEMPLATE;
        }
        return restTemplate;
    }
}
