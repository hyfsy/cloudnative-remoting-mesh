package com.hyf.cloudnative.remoting.mesh.proxy;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class K8SClientFactoryBean extends ClientConfig implements FactoryBean<Object>, InitializingBean, BeanFactoryAware {

    private static final List<ProxyProvider> proxyProviders = new ArrayList<>();

    static {
        ServiceLoader<ProxyProvider> providers = ServiceLoader.load(ProxyProvider.class);
        providers.forEach(proxyProviders::add);
    }

    private BeanFactory beanFactory;

    @Override
    public Object getObject() {
        return getProxy();
    }

    @SuppressWarnings("unchecked")
    private <T> T getProxy() {
        Assert.notNull(beanFactory, "BeanFactory is null");

        super.validate(); // 校验配置
        super.fillDefaultValue(); // 填充默认值

        for (ProxyProvider provider : proxyProviders) {
            if (getRequestWay() == provider.requestWay()) {

                return (T) provider.get(beanFactory, this);
            }
        }

        throw new IllegalArgumentException("RequestWay not support: " + getRequestWay());
    }

    @Override
    public Class<?> getObjectType() {
        return getType();
    }

    // 手动注入
    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        super.validate(); // 校验配置
    }
}
