package com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.server;

import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.NamedThreadFactory;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author baB_hyf
 * @date 2022/10/01
 */
public class GrpcServer {

    private final List<BindableService> bindableServices = new ArrayList<>();

    private final AtomicBoolean stopped = new AtomicBoolean(true);

    private final GrpcServerConfig grpcServerConfig;

    private Server server;

    private int port;

    public GrpcServer() {
        this(new GrpcServerConfig());
    }

    public GrpcServer(GrpcServerConfig grpcServerConfig) {
        this.grpcServerConfig = grpcServerConfig;
    }

    public void start() {

        if (!stopped.compareAndSet(true, false)) {
            return;
        }

        ThreadPoolExecutor grpcExecutor = new ThreadPoolExecutor(
                grpcServerConfig.getThreadPoolSize(),
                grpcServerConfig.getThreadPoolSize(),
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(grpcServerConfig.getThreadPoolQueueSize()),
                new NamedThreadFactory("grpc-server-executor", grpcServerConfig.getThreadPoolSize()));

        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(grpcServerConfig.getListenPort())
                .executor(grpcExecutor)
                .maxInboundMessageSize(grpcServerConfig.getMaxInboundMessageSize());
        bindableServices.forEach(serverBuilder::addService);
        server = serverBuilder.build();

        try {
            server.start();
        } catch (IOException e) {
            throw new RuntimeException("Failed to start grpc server", e);
        }

        port = server.getPort();
    }

    public void stop() {
        if (!stopped.compareAndSet(false, true)) {
            return;
        }

        if (server != null) {
            server.shutdownNow();
        }
    }

    public List<BindableService> getBindableServices() {
        return bindableServices;
    }

    public boolean isStopped() {
        return stopped.get();
    }

    public GrpcServerConfig getGrpcServerConfig() {
        return grpcServerConfig;
    }

    public int getPort() {
        return port;
    }
}
