package com.hyf.cloudnative.remoting.mesh.proxy;

import com.hyf.cloudnative.remoting.mesh.RemotingProperties;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

import static com.hyf.cloudnative.remoting.mesh.proxy.ClientConfigUtils.*;

/**
 * TODO 赋值逻辑优化
 */
public class K8SClientFactoryBean extends ClientConfig implements FactoryBean<Object>, BeanFactoryAware, EnvironmentAware {

    private static final List<ProxyProvider> proxyProviders = new ArrayList<>();

    private BeanFactory beanFactory;
    private Environment environment;

    private Map<String, Object> lazyAttributes; // only use for spring lifecycle

    static {
        ServiceLoader<ProxyProvider> providers = ServiceLoader.load(ProxyProvider.class);
        providers.forEach(proxyProviders::add);
    }

    @Override
    public Object getObject() {
        return getProxy();
    }

    @Override
    public Class<?> getObjectType() {
        return getType();
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public void setLazyAttributes(Map<String, Object> lazyAttributes) {
        this.lazyAttributes = lazyAttributes;
    }

    @SuppressWarnings("unchecked")
    private <T> T getProxy() {
        Assert.notNull(beanFactory, "BeanFactory is null");

        // init
        fillDefaultValueByProperties(beanFactory); // 填充 properties 的值
        injectLazyAttributes(); // 填充 annotation 的值
        super.validate(); // 校验配置

        // create
        for (ProxyProvider provider : proxyProviders) {
            if (getRequestWay().equalsIgnoreCase(provider.requestWay())) {

                return (T) provider.get(beanFactory, this);
            }
        }

        throw new IllegalArgumentException("RequestWay not support: " + getRequestWay());
    }

    private void fillDefaultValueByProperties(BeanFactory beanFactory) {
        RemotingProperties properties = beanFactory.getBean(RemotingProperties.class);
        setIfHasText(properties.getRequestWay(), this::setRequestWay);
        setIfHasText(properties.getNamespace(), this::setNamespace);
        setIfHasText(properties.getClusterDomain(), this::setClusterDomain);
    }

    private void injectLazyAttributes() {
        if (!CollectionUtils.isEmpty(lazyAttributes)) {
            Map<String, Object> attributes = lazyAttributes;
            // set use annotation value
            setIfNotNull(getServiceHost(beanFactory, attributes), this::setServiceName);
            setIfNotNull((Boolean) attributes.get("tlsEnable"), this::setTlsEnable);
            setIfHasText((String) attributes.get("namespace"), this::setNamespace);
            setIfHasText((String) attributes.get("clusterDomain"), this::setClusterDomain);
            setIfHasText((String) attributes.get("requestWay"), requestWay -> {
                K8SClientFactoryBean.this.setRequestWay(requestWay);
                K8SClientFactoryBean.this.setDefaultPortUseRequestWay(requestWay);
            });
            setIfPositive(getServicePort(beanFactory, attributes), this::setPort);
            setIfNotVoidClass((Class<?>) attributes.get("fallback"), this::setFallback);
            setIfNotVoidClass((Class<?>) attributes.get("fallbackFactory"), this::setFallbackFactory);
        }
    }

    private String getServiceHost(BeanFactory beanFactory, Map<String, Object> attributes) {
        if (StringUtils.hasText((String) attributes.get("value"))) {
            return resolve(beanFactory, (String) attributes.get("value"));
        } else {
            return resolve(beanFactory, (String) attributes.get("name"));
        }
    }

    private Integer getServicePort(BeanFactory beanFactory, Map<String, Object> attributes) {
        String port = ((String) attributes.get("port"));
        if (!StringUtils.hasText(port)) {
            return null;
        }
        try {
            return Integer.parseInt(resolve(beanFactory, port.trim()));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Client port is invalid: " + attributes.get("port"));
        }
    }

    private String resolve(BeanFactory beanFactory, String value) {
        if (environment != null && StringUtils.hasText(value)) {
            return this.environment.resolvePlaceholders(value);
        }
        return value;
    }
}
