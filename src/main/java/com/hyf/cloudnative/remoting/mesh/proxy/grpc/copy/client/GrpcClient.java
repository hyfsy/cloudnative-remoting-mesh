package com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client;

import com.hyf.cloudnative.remoting.mesh.proxy.grpc.Message;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.ConnectionManager;
import com.hyf.cloudnative.remoting.mesh.exception.RemotingException;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.utils.RemotingUtils;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.ConnectionWrapper;
import com.google.common.util.concurrent.ListenableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author baB_hyf
 * @date 2022/10/01
 */
public class GrpcClient {

    private static final Logger log = LoggerFactory.getLogger(GrpcClient.class);

    private final GrpcClientConfig grpcClientConfig;
    private final ConnectionManager connectionManager;

    public GrpcClient() {
        this(new GrpcClientConfig());
    }

    public GrpcClient(GrpcClientConfig grpcClientConfig) {
        this.grpcClientConfig = grpcClientConfig;
        this.connectionManager = new ConnectionManager(grpcClientConfig);
    }

    public void start() {
        connectionManager.start();
    }

    public void stop() {
        connectionManager.destroy();
    }

    public Message request(String addr, Message message) throws Throwable {

        Throwable ex = null;

        ConnectionWrapper connection = connectionManager.getOrCreateConnection(addr);
        try {
            ListenableFuture<com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message> responseFuture = connection.getFutureStub().request(RemotingUtils.convert(message));
            com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message response = responseFuture.get();
            if (!response.getEvent()) {
                return RemotingUtils.convert(response);
            }
            // TODO event response handle
        } catch (Exception e) {
            connectionManager.closeConnection(connection);
            ex = e;
        }

        // TODO 日志打印优化，被spring捕获到，会丢失当前调用的堆栈
        if (ex != null && log.isDebugEnabled()) {
            log.debug("Failed to request", ex);
        }

        if (ex != null) {
            throw ex;
        } else {
            throw new RemotingException("Cannot happen");
        }
    }

    public GrpcClientConfig getGrpcClientConfig() {
        return grpcClientConfig;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
