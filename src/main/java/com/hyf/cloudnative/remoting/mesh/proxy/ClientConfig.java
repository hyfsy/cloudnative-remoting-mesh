package com.hyf.cloudnative.remoting.mesh.proxy;

import com.hyf.cloudnative.remoting.mesh.RequestWay;
import com.hyf.cloudnative.remoting.mesh.constants.ClientConstants;
import com.hyf.cloudnative.remoting.mesh.utils.ServiceHostUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.util.Assert;

/**
 * k8s client build config.
 */
public class ClientConfig {

    /**
     * client interface type
     */
    private Class<?> type;

    /**
     * requested k8s service name
     */
    private String serviceName;

    /**
     * requested k8s service port
     */
    private int port = -1;

    /**
     * whether the client supports tls
     */
    private boolean tlsEnable;

    /**
     * requested k8s namespace
     */
    private String namespace;

    /**
     * requested k8s cluster domain
     */
    private String clusterDomain;

    /**
     * client request way
     */
    private String requestWay;

    /**
     * client request fallback instance type
     */
    private Class<?> fallback;

    /**
     * client request fallback factory instance type
     */
    private Class<?> fallbackFactory;

    public ClientConfig() {
        this.fillDefaultValue(); // 填充默认值
    }

    private void fillDefaultValue() {
        requestWay = ClientConstants.DEFAULT_REQUEST_WAY;
        tlsEnable = ClientConstants.DEFAULT_TLS_ENABLE;
        namespace = ClientConstants.DEFAULT_NAMESPACE;
        clusterDomain = ClientConstants.DEFAULT_CLUSTER_DOMAIN;
        setDefaultPortUseRequestWay(requestWay);
    }

    public void setDefaultPortUseRequestWay(String requestWay) {
        port = RequestWay.HTTP.equalsIgnoreCase(requestWay) ?
                (tlsEnable ? ClientConstants.DEFAULT_HTTPS_PORT : ClientConstants.DEFAULT_HTTP_PORT) :
                ClientConstants.DEFAULT_GRPC_PORT;
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

    public String getRequestWay() {
        return requestWay;
    }

    public void setRequestWay(String requestWay) {
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
