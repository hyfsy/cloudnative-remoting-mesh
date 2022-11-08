package com.hyf.cloudnative.remoting.mesh.utils;

import com.hyf.cloudnative.remoting.mesh.RemotingProperties;
import com.hyf.cloudnative.remoting.mesh.constants.ClientConstants;
import org.springframework.util.StringUtils;

public class ServiceHostUtils {

    private static volatile RemotingProperties properties;

    public ServiceHostUtils(RemotingProperties properties) {
        this.properties = properties;
    }

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
