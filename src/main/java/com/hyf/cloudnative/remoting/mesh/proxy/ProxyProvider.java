package com.hyf.cloudnative.remoting.mesh.proxy;

import org.springframework.beans.factory.BeanFactory;

/**
 * extension point, used to create client proxy by different request way.
 *
 * @see com.hyf.cloudnative.remoting.mesh.RequestWay
 */
public interface ProxyProvider {

    /**
     * request way, string type, distinguish between different ways.
     *
     * @return specified request way
     */
    String requestWay();

    /**
     * get different client proxy instances based on the {@link ClientConfig} parameter.
     *
     * @param beanFactory  Spring beanFactory
     * @param clientConfig client config
     * @return client proxy instance
     */
    Object get(BeanFactory beanFactory, ClientConfig clientConfig);
}
