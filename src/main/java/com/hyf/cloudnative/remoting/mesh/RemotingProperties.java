package com.hyf.cloudnative.remoting.mesh;

import com.hyf.cloudnative.remoting.mesh.constants.ClientConstants;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client.GrpcClientConfig;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.server.GrpcServerConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(ClientConstants.PROPERTIES_PREFIX)
public class RemotingProperties {

    public String namespace = ClientConstants.DEFAULT_NAMESPACE;

    public String clusterDomain = ClientConstants.DEFAULT_CLUSTER_DOMAIN;

    public final Grpc grpc = new Grpc();

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

    public static class Grpc {

        @NestedConfigurationProperty
        public final GrpcClientConfig client = new GrpcClientConfig();

        @NestedConfigurationProperty
        public final GrpcServerConfig server = new GrpcServerConfig();

        public GrpcClientConfig getClient() {
            return client;
        }

        public GrpcServerConfig getServer() {
            return server;
        }
    }

    public Grpc getGrpc() {
        return grpc;
    }
}
