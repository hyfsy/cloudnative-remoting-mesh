package com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client;

import com.hyf.cloudnative.remoting.mesh.constants.ClientConstants;

/**
 * @author baB_hyf
 * @date 2022/10/01
 */
public class GrpcClientConfig {

    public static final String GRPC_PROPERTIES_PREFIX = ClientConstants.PROPERTIES_PREFIX + ".grpc.client";

    private int threadPoolSize = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".threadPoolSize", String.valueOf(Runtime.getRuntime().availableProcessors() * 4)));
    private int maxInboundMessageSize = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".maxInboundMessageSize", String.valueOf(10 * 1024 * 1024)));
    private int keepAliveTimeMillis = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".keepAliveTimeMillis", String.valueOf(6 * 60 * 1000)));

    private long requestTimeoutMillis = Long.parseLong(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".requestTimeoutMillis", String.valueOf(60_000L)));

    private boolean retryEnable = Boolean.parseBoolean(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".retryEnable", String.valueOf(false)));
    private int retryTimes = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".retryTimes", String.valueOf(3)));

    private long heartbeatIntervalMillis = Long.parseLong(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".heartbeatIntervalMillis", String.valueOf(3000L)));
    private long heartbeatTimeoutMillis = Long.parseLong(System.getProperty(
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

    public long getRequestTimeoutMillis() {
        return requestTimeoutMillis;
    }

    public void setRequestTimeoutMillis(long requestTimeoutMillis) {
        this.requestTimeoutMillis = requestTimeoutMillis;
    }

    public boolean isRetryEnable() {
        return retryEnable;
    }

    public void setRetryEnable(boolean retryEnable) {
        this.retryEnable = retryEnable;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
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
