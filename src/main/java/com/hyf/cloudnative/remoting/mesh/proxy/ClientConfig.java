package com.hyf.cloudnative.remoting.mesh.proxy;

import com.hyf.cloudnative.remoting.mesh.RequestWay;
import com.hyf.cloudnative.remoting.mesh.constants.ClientConstants;
import com.hyf.cloudnative.remoting.mesh.utils.ServiceHostUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;

public class ClientConfig {

    private Class<?> type;
    private String serviceName;
    private int port;
    private boolean tlsEnable = ClientConstants.DEFAULT_TLS_ENABLE;
    private String namespace;
    private String clusterDomain;
    private RequestWay requestWay;
    private Class<?> fallback;
    private Class<?> fallbackFactory;

    public void fillDefaultValue() {
        if (requestWay == null) {
            requestWay = ClientConstants.DEFAULT_REQUEST_WAY;
        }
        if (port <= 0) {
            port = requestWay == RequestWay.HTTP ?
                    (tlsEnable ? ClientConstants.DEFAULT_HTTPS_PORT : ClientConstants.DEFAULT_HTTP_PORT) :
                    ClientConstants.DEFAULT_GRPC_PORT;
        }
//        // properties -> default, here no need set default value
//        if (!StringUtils.hasText(namespace)) {
//            namespace = ClientConstants.DEFAULT_NAMESPACE;
//        }
//        if (!StringUtils.hasText(clusterDomain)) {
//            clusterDomain = ClientConstants.DEFAULT_CLUSTER_DOMAIN;
//        }
    }

    public void validate() {
        Assert.notNull(type, "Type is null");
        Assert.hasText(serviceName, "ServiceName is null");
    }

    public String generateServiceHost(BeanFactory beanFactory) {
        ServiceHostUtils serviceHostUtils = beanFactory.getBean(ServiceHostUtils.class);
        return serviceHostUtils.generateServiceHost(serviceName, namespace, clusterDomain);
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isTlsEnable() {
        return tlsEnable;
    }

    public void setTlsEnable(boolean tlsEnable) {
        this.tlsEnable = tlsEnable;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getClusterDomain() {
        return clusterDomain;
    }

    public void setClusterDomain(String clusterDomain) {
        this.clusterDomain = clusterDomain;
    }

    public RequestWay getRequestWay() {
        return requestWay;
    }

    public void setRequestWay(RequestWay requestWay) {
        this.requestWay = requestWay;
    }

    public Class<?> getFallback() {
        return fallback;
    }

    public void setFallback(Class<?> fallback) {
        this.fallback = fallback;
    }

    public Class<?> getFallbackFactory() {
        return fallbackFactory;
    }

    public void setFallbackFactory(Class<?> fallbackFactory) {
        this.fallbackFactory = fallbackFactory;
    }
}
