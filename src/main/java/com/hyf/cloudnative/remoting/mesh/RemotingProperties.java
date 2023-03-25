package com.hyf.cloudnative.remoting.mesh;

import com.hyf.cloudnative.remoting.mesh.constants.ClientConstants;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client.GrpcClientConfig;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.server.GrpcServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

/**
 * remoting call related properties.
 */
@ConfigurationProperties(ClientConstants.PROPERTIES_PREFIX)
public class RemotingProperties {

    /**
     * grpc related config
     */
    public final Grpc grpc = new Grpc();

    /**
     * k8s namespace
     */
    public String namespace;

    /**
     * k8s cluster domain
     */
    public String clusterDomain;

    /**
     * k8s client request way
     */
    public String requestWay;

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

    public Grpc getGrpc() {
        return grpc;
    }

    public static class Grpc {

        /**
         * grpc client config
         */
        @NestedConfigurationProperty
        public final GrpcClientConfig client = new GrpcClientConfig();

        /**
         * grpc server config
         */
        @NestedConfigurationProperty
        public final GrpcServerConfig server = new GrpcServerConfig();

        public GrpcClientConfig getClient() {
            return client;
        }

        public GrpcServerConfig getServer() {
            return server;
        }
    }
}
