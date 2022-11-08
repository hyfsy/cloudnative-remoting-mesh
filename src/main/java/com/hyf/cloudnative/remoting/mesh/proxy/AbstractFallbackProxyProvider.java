package com.hyf.cloudnative.remoting.mesh.proxy;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public abstract class AbstractFallbackProxyProvider implements ProxyProvider {

    @Override
    public Object get(BeanFactory beanFactory, ClientConfig clientConfig) {

        InvocationHandler invocationHandler = createInvocationHandler(beanFactory, clientConfig);

        // fallback
        if (clientConfig.getFallback() != void.class) {
            Object fallbackInstance = getBeanFromContext(beanFactory, clientConfig.getFallback(), clientConfig.getType());
            FallbackFactory.Default<Object> factory = new FallbackFactory.Default<>(fallbackInstance);
            invocationHandler = new FallbackInvocationHandler(invocationHandler, factory);
        }
        // fallback factory
        else if (clientConfig.getFallbackFactory() != void.class) {
            FallbackFactory<?> fallbackFactoryInstance = getBeanFromContext(beanFactory, clientConfig.getFallbackFactory(), FallbackFactory.class);
            invocationHandler = new FallbackInvocationHandler(invocationHandler, fallbackFactoryInstance);
        }
        // default null
        else {
            invocationHandler = new FallbackInvocationHandler(invocationHandler, null);
        }

        return createProxy(beanFactory, clientConfig, invocationHandler);
    }

    protected abstract InvocationHandler createInvocationHandler(BeanFactory beanFactory, ClientConfig clientConfig);

    protected Object createProxy(BeanFactory beanFactory, ClientConfig clientConfig, InvocationHandler invocationHandler) {
        return Proxy.newProxyInstance(clientConfig.getType().getClassLoader(), new Class[]{clientConfig.getType()}, invocationHandler);
    }

    @SuppressWarnings("unchecked")
    private <T> T getBeanFromContext(BeanFactory beanFactory, Class<?> beanClass, Class<T> needType) {

        Object fallbackBean = null;
        try {
            fallbackBean = beanFactory.getBean(beanClass);
        } catch (NoSuchBeanDefinitionException ignored) {
        }

        if (fallbackBean == null) {
            throw new IllegalStateException(String.format("No instance of type %s found for k8s client", beanClass));
        }
        if (!needType.isAssignableFrom(fallbackBean.getClass())) {
            throw new IllegalStateException(String.format("Fallback of type %s is not assignable to %s for k8s client", fallbackBean.getClass(), needType));
        }

        return (T) fallbackBean;
    }
}
