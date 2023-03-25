package com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy;

import com.google.common.util.concurrent.ListenableFuture;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client.GrpcClientConfig;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.RemotingApiGrpc;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.utils.RemotingUtils;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author baB_hyf
 * @date 2022/10/01
 */
public class ConnectionManager {

    private static final Logger log = LoggerFactory.getLogger(ConnectionManager.class);

    private final Map<String, ConnectionWrapper> connectionTables = new ConcurrentHashMap<>(128);

    private final GrpcClientConfig grpcClientConfig;

    private final ScheduledExecutorService heartbeatExecutor;

    public ConnectionManager(GrpcClientConfig grpcClientConfig) {
        this.grpcClientConfig = grpcClientConfig;
        this.heartbeatExecutor = Executors.newSingleThreadScheduledExecutor(new NamedThreadFactory("hotrefresh-grpc-connection-heartbeat-executor", 1));
    }

    public void start() {
        this.heartbeatExecutor.scheduleWithFixedDelay(() -> {
            try {
                healthCheck();
            } catch (Throwable t) {
                log.error("Failed to process health check", t);
            }
        }, grpcClientConfig.getHeartbeatIntervalMillis(), grpcClientConfig.getHeartbeatIntervalMillis(), TimeUnit.MILLISECONDS);
    }

    public ConnectionWrapper getOrCreateConnection(String addr) {
        connectionTables.computeIfAbsent(addr, (add) -> createConnection(addr));
        return connectionTables.get(addr);
    }

    private ConnectionWrapper createConnection(String addr) {
        ManagedChannel managedChannel = createManagedChannel(addr);
        RemotingApiGrpc.RemotingApiFutureStub futureStub = createFutureStub(managedChannel);
        return new ConnectionWrapper(addr, managedChannel, futureStub);
    }

    private ManagedChannel createManagedChannel(String addr) {
        InetSocketAddress address = RemotingUtils.parseSocketAddress(addr);
        // System.out.println(addr);
        // System.out.println(address);
        // System.out.println(address.getHostName());
        // System.out.println(address.getPort());
        // log.info(addr);
        // log.info(address.toString());
        // log.info(address.getHostName());
        // log.info(address.getPort() + "");
        return ManagedChannelBuilder
                .forAddress(address.getHostName(), address.getPort())
                .maxInboundMessageSize(grpcClientConfig.getMaxInboundMessageSize())
                .keepAliveTime(grpcClientConfig.getKeepAliveTimeMillis(), TimeUnit.MILLISECONDS)
                .usePlaintext()
                .build();
    }

    private RemotingApiGrpc.RemotingApiFutureStub createFutureStub(ManagedChannel managedChannel) {
        return RemotingApiGrpc.newFutureStub(managedChannel);
    }

    private void healthCheck() {

        if (connectionTables.isEmpty()) {
            return;
        }

        Iterator<ConnectionWrapper> it = connectionTables.values().iterator();
        while (it.hasNext()) {
            ConnectionWrapper connection = it.next();

            boolean health = false;
            try {
                ListenableFuture<Message> responseFuture = connection.getFutureStub().request(RemotingUtils.PING);
                Message response = responseFuture.get(grpcClientConfig.getHeartbeatTimeoutMillis(), TimeUnit.MILLISECONDS);
                String msg = response.getMetadataOrDefault(RemotingUtils.HEALTH_CHECK_METADATA_K, "");
                health = RemotingUtils.PONG.getMetadataMap().get(RemotingUtils.HEALTH_CHECK_METADATA_K).equals(msg);
            } catch (Exception ignored) {
            }

            if (!health) {
                it.remove();
            }
        }
    }

    public void closeConnection(ConnectionWrapper connection) {
        if (connection == null) {
            return;
        }
        connectionTables.remove(connection.getAddr());
        connection.close();
    }

    public void destroy() {
        heartbeatExecutor.shutdown();
    }

}
