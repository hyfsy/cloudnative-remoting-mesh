package com.hyf.cloudnative.remoting.mesh;

import com.hyf.cloudnative.remoting.mesh.proxy.K8SClientFactoryBean;
import org.springframework.context.ApplicationContext;

public class K8SClientBuilder {

    private final ApplicationContext applicationContext;

    public K8SClientBuilder(final ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public <T> Builder<T> forType(final Class<T> type, final String name) {
        return new Builder<>(this.applicationContext, type, name);
    }

    public <T> Builder<T> forType(final Class<T> type, final K8SClientFactoryBean clientFactoryBean,
                                                     final String name) {
        return new Builder<>(this.applicationContext, clientFactoryBean, type, name);
    }

    /**
     * Builder of feign targets.
     *
     * @param <T> type of target
     */
    public static final class Builder<T> {

        private final K8SClientFactoryBean k8sClientFactoryBean;

        private Builder(final ApplicationContext applicationContext, final Class<T> type, final String name) {
            this(applicationContext, new K8SClientFactoryBean(), type, name);
        }

        private Builder(final ApplicationContext applicationContext, final K8SClientFactoryBean clientFactoryBean,
                        final Class<T> type, final String name) {
            this.k8sClientFactoryBean = clientFactoryBean;

            this.k8sClientFactoryBean.setBeanFactory(applicationContext);
            this.k8sClientFactoryBean.setType(type);
            this.k8sClientFactoryBean.setServiceName(name);
        }

        public K8SClientBuilder.Builder<T> port(final int port) {
            this.k8sClientFactoryBean.setPort(port);
            return this;
        }

        public K8SClientBuilder.Builder<T> tlsEnable(final boolean tlsEnable) {
            this.k8sClientFactoryBean.setTlsEnable(tlsEnable);
            return this;
        }

        public K8SClientBuilder.Builder<T> namespace(final String namespace) {
            this.k8sClientFactoryBean.setNamespace(namespace);
            return this;
        }

        public K8SClientBuilder.Builder<T> clusterDomain(final String clusterDomain) {
            this.k8sClientFactoryBean.setClusterDomain(clusterDomain);
            return this;
        }

        public K8SClientBuilder.Builder<T> requestWay(final RequestWay requestWay) {
            this.k8sClientFactoryBean.setRequestWay(requestWay);
            return this;
        }

        public K8SClientBuilder.Builder<T> fallback(Class<? extends T> fallback) {
            this.k8sClientFactoryBean.setFallback(fallback);
            return this;
        }

        public K8SClientBuilder.Builder<T> fallbackFactory(Class<? extends T> fallbackFactory) {
            this.k8sClientFactoryBean.setFallbackFactory(fallbackFactory);
            return this;
        }

        @SuppressWarnings("unchecked")
        public T build() {
            return (T) this.k8sClientFactoryBean.getObject();
        }
    }
}
