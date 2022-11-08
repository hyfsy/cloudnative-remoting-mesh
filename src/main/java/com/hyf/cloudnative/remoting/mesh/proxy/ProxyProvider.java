package com.hyf.cloudnative.remoting.mesh.proxy;

import com.hyf.cloudnative.remoting.mesh.RequestWay;
import org.springframework.beans.factory.BeanFactory;

public interface ProxyProvider {
    RequestWay requestWay();
    Object get(BeanFactory beanFactory, ClientConfig clientConfig);
}
