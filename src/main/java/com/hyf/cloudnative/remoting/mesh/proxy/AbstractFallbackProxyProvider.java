package com.hyf.cloudnative.remoting.mesh.proxy;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * wrap original {@link ProxyProvider}, supply fallback implementation.
 * <p>
 * users are recommended to implement this class.
 *
 * @see FallbackInvocationHandler
 */
public abstract class AbstractFallbackProxyProvider implements ProxyProvider {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(BeanFactory beanFactory, ClientConfig clientConfig) {

        InvocationHandler invocationHandler = createInvocationHandler(beanFactory, clientConfig);

        // fallback
        if (clientConfig.getFallback() != null && clientConfig.getFallback() != void.class) {
            Object fallbackInstance = getBeanFromContext(beanFactory, clientConfig.getFallback(), clientConfig.getType());
            FallbackFactory.Default<Object> factory = new FallbackFactory.Default<>(fallbackInstance);
            invocationHandler = new FallbackInvocationHandler(invocationHandler, factory);
        }
        // fallback factory
        else if (clientConfig.getFallbackFactory() != null && clientConfig.getFallbackFactory() != void.class) {
            FallbackFactory<?> fallbackFactoryInstance = getBeanFromContext(beanFactory, clientConfig.getFallbackFactory(), FallbackFactory.class);
            invocationHandler = new FallbackInvocationHandler(invocationHandler, fallbackFactoryInstance);
        }
        // default null
        else {
            invocationHandler = new FallbackInvocationHandler(invocationHandler, null);
        }

        return createProxy(beanFactory, clientConfig, invocationHandler);
    }

    /**
     * create invocation handler instance for invoke network calls.
     *
     * @param beanFactory  Spring beanFactory
     * @param clientConfig client config
     * @return invocation handler instance
     */
    protected abstract InvocationHandler createInvocationHandler(BeanFactory beanFactory, ClientConfig clientConfig);

    /**
     * create the client proxy instance, default uses jdk proxy simply.
     *
     * @param beanFactory       Spring beanFactory
     * @param clientConfig      client config
     * @param invocationHandler invocation handler instance
     * @return client proxy instance
     */
    protected Object createProxy(BeanFactory beanFactory, ClientConfig clientConfig, InvocationHandler invocationHandler) {
        return Proxy.newProxyInstance(clientConfig.getType().getClassLoader(), new Class[]{clientConfig.getType()}, invocationHandler);
    }

    @SuppressWarnings("unchecked")
    private <T> T getBeanFromContext(BeanFactory beanFactory, Class<?> beanClass, Class<T> needType) {

        Object bean = null;
        try {
            bean = beanFactory.getBean(beanClass);
        } catch (NoSuchBeanDefinitionException ignored) {
        }

        if (bean == null) {
            throw new IllegalStateException(String.format("No instance of type %s found for create proxy instance", beanClass));
        }
        if (!needType.isAssignableFrom(bean.getClass())) {
            throw new IllegalStateException(String.format("Bean type %s is not assignable to %s for create proxy instance", bean.getClass(), needType));
        }

        return (T) bean;
    }
}
