package com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.server;

import com.hyf.cloudnative.remoting.mesh.constants.ClientConstants;

/**
 * grpc server config.
 *
 * @author baB_hyf
 * @date 2022/10/01
 */
public class GrpcServerConfig {

    public static final String GRPC_PROPERTIES_PREFIX = ClientConstants.PROPERTIES_PREFIX + ".grpc.server";

    /**
     * grpc server port
     */
    private int listenPort            = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".listenPort", String.valueOf(ClientConstants.DEFAULT_GRPC_PORT)));
    /**
     * max inbound message size
     */
    private int maxInboundMessageSize = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".maxInboundMessageSize", String.valueOf(10 * 1024 * 1024)));
    /**
     * thread pool size
     */
    private int threadPoolSize        = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".threadPoolSize", String.valueOf(Runtime.getRuntime().availableProcessors() * 4)));
    /**
     * thread pool queue size
     */
    private int threadPoolQueueSize   = Integer.parseInt(System.getProperty(
            GRPC_PROPERTIES_PREFIX + ".threadPoolQueueSize", String.valueOf(1 << 14))); // 16384

    public int getListenPort() {
        return listenPort;
    }

    public void setListenPort(int listenPort) {
        this.listenPort = listenPort;
    }

    public int getMaxInboundMessageSize() {
        return maxInboundMessageSize;
    }

    public void setMaxInboundMessageSize(int maxInboundMessageSize) {
        this.maxInboundMessageSize = maxInboundMessageSize;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public int getThreadPoolQueueSize() {
        return threadPoolQueueSize;
    }

    public void setThreadPoolQueueSize(int threadPoolQueueSize) {
        this.threadPoolQueueSize = threadPoolQueueSize;
    }
}
