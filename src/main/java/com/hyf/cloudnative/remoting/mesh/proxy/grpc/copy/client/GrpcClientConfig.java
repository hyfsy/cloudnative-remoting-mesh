package com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client;

import com.hyf.cloudnative.remoting.mesh.constants.ClientConstants;

/**
 * grpc client config.
 *
 * @author baB_hyf
 * @date 2022/10/01
 */
public class GrpcClientConfig {

    public static final String GRPC_PROPERTIES_PREFIX = ClientConstants.PROPERTIES_PREFIX + ".grpc.client";

    /**
     * thread pool size
     */
    private int threadPoolSize        = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".threadPoolSize", String.valueOf(Runtime.getRuntime().availableProcessors() * 4)));
    /**
     * max inbound message size
     */
    private int maxInboundMessageSize = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".maxInboundMessageSize", String.valueOf(10 * 1024 * 1024)));
    /**
     * keep alive time(millis) of the client connection
     */
    private int keepAliveTimeMillis   = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".keepAliveTimeMillis", String.valueOf(6 * 60 * 1000)));

    /**
     * heartbeat interval of the client connection
     */
    private long heartbeatIntervalMillis = Long.parseLong(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".heartbeatIntervalMillis", String.valueOf(3000L)));
    /**
     * heartbeat timeout time of the client connection
     */
    private long heartbeatTimeoutMillis  = Long.parseLong(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".heartbeatTimeoutMillis", String.valueOf(3000L)));

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public int getMaxInboundMessageSize() {
        return maxInboundMessageSize;
    }

    public void setMaxInboundMessageSize(int maxInboundMessageSize) {
        this.maxInboundMessageSize = maxInboundMessageSize;
    }

    public int getKeepAliveTimeMillis() {
        return keepAliveTimeMillis;
    }

    public void setKeepAliveTimeMillis(int keepAliveTimeMillis) {
        this.keepAliveTimeMillis = keepAliveTimeMillis;
    }

    public long getHeartbeatIntervalMillis() {
        return heartbeatIntervalMillis;
    }

    public void setHeartbeatIntervalMillis(long heartbeatIntervalMillis) {
        this.heartbeatIntervalMillis = heartbeatIntervalMillis;
    }

    public long getHeartbeatTimeoutMillis() {
        return heartbeatTimeoutMillis;
    }

    public void setHeartbeatTimeoutMillis(long heartbeatTimeoutMillis) {
        this.heartbeatTimeoutMillis = heartbeatTimeoutMillis;
    }
}
