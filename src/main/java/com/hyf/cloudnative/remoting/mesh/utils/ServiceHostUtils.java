package com.hyf.cloudnative.remoting.mesh.utils;

import com.hyf.cloudnative.remoting.mesh.RemotingProperties;
import com.hyf.cloudnative.remoting.mesh.constants.ClientConstants;
import org.springframework.util.StringUtils;

/**
 * get service host in k8s svc communication url format.
 */
public class ServiceHostUtils {

    private final RemotingProperties properties;

    public ServiceHostUtils(RemotingProperties properties) {
        this.properties = properties;
    }

    /**
     * generate service host address, priority: specific > properties > default
     *
     * @param serviceName   k8s service name
     * @param namespace     k8s namespace
     * @param clusterDomain k8s cluster domain
     * @return service host address, like serviceName.namespace.svc.clusterDomain
     */
    public String generateServiceHost(String serviceName, String namespace, String clusterDomain) {
        if (!StringUtils.hasText(namespace)) {
            namespace = properties.getNamespace();
            if (!StringUtils.hasText(namespace)) {
                namespace = ClientConstants.DEFAULT_NAMESPACE;
            }
        }
        if (!StringUtils.hasText(clusterDomain)) {
            clusterDomain = properties.getClusterDomain();
            if (!StringUtils.hasText(clusterDomain)) {
                clusterDomain = ClientConstants.DEFAULT_CLUSTER_DOMAIN;
            }
        }
        return serviceName + "." + namespace + ".svc." + clusterDomain;
    }
}
