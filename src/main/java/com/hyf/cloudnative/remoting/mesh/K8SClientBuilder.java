package com.hyf.cloudnative.remoting.mesh;

import com.hyf.cloudnative.remoting.mesh.proxy.K8SClientFactoryBean;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

/**
 * create K8SClient in hard code
 * <p>
 * TODO 赋值逻辑优化
 *
 * @see K8SClient
 */
public class K8SClientBuilder {

    private final ApplicationContext applicationContext;

    public K8SClientBuilder(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> Builder<T> forType(Class<T> type, String name) {
        return new Builder<>(this.applicationContext, type, name);
    }

    /**
     * Builder of feign targets.
     *
     * @param <T> type of target
     */
    public static final class Builder<T> {

        private final K8SClientFactoryBean k8sClientFactoryBean;
        private final ApplicationContext   applicationContext;
        private final Class<T>             type;

        private final Map<String, Object> lazyAttributes = new HashMap<>();

        private Builder(ApplicationContext applicationContext, Class<T> type, String name) {
            this(applicationContext, new K8SClientFactoryBean(), type, name);
        }

        private Builder(ApplicationContext applicationContext, K8SClientFactoryBean clientFactoryBean, Class<T> type, String name) {
            this.k8sClientFactoryBean = clientFactoryBean;
            this.applicationContext = applicationContext;
            this.type = type;

            // this.k8sClientFactoryBean.setServiceName(name);
            this.lazyAttributes.put("name", name);
        }

        public K8SClientBuilder.Builder<T> port(int port) {
            // this.k8sClientFactoryBean.setPort(port);
            this.lazyAttributes.put("port", port);
            return this;
        }

        public K8SClientBuilder.Builder<T> tlsEnable(boolean tlsEnable) {
            // this.k8sClientFactoryBean.setTlsEnable(tlsEnable);
            this.lazyAttributes.put("tlsEnable", tlsEnable);
            return this;
        }

        public K8SClientBuilder.Builder<T> namespace(String namespace) {
            // this.k8sClientFactoryBean.setNamespace(namespace);
            this.lazyAttributes.put("namespace", namespace);
            return this;
        }

        public K8SClientBuilder.Builder<T> clusterDomain(String clusterDomain) {
            // this.k8sClientFactoryBean.setClusterDomain(clusterDomain);
            this.lazyAttributes.put("clusterDomain", clusterDomain);
            return this;
        }

        public K8SClientBuilder.Builder<T> requestWay(String requestWay) {
            // this.k8sClientFactoryBean.setRequestWay(requestWay);
            this.lazyAttributes.put("requestWay", requestWay);
            return this;
        }

        public K8SClientBuilder.Builder<T> fallback(Class<? extends T> fallback) {
            // this.k8sClientFactoryBean.setFallback(fallback);
            this.lazyAttributes.put("fallback", fallback);
            return this;
        }

        public K8SClientBuilder.Builder<T> fallbackFactory(Class<? extends T> fallbackFactory) {
            // this.k8sClientFactoryBean.setFallbackFactory(fallbackFactory);
            this.lazyAttributes.put("fallbackFactory", fallbackFactory);
            return this;
        }

        @SuppressWarnings("unchecked")
        public T build() {

            // properties set
            this.k8sClientFactoryBean.setBeanFactory(applicationContext);
            this.k8sClientFactoryBean.setEnvironment(applicationContext.getEnvironment());
            this.k8sClientFactoryBean.setType(type);
            this.k8sClientFactoryBean.setLazyAttributes(this.lazyAttributes);

            // create proxy
            return (T) this.k8sClientFactoryBean.getObject();
        }
    }
}
