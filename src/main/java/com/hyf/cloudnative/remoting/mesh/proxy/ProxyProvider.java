package com.hyf.cloudnative.remoting.mesh.proxy;

import org.springframework.beans.factory.BeanFactory;

public interface ProxyProvider {
    String requestWay();
    Object get(BeanFactory beanFactory, ClientConfig clientConfig);
}
