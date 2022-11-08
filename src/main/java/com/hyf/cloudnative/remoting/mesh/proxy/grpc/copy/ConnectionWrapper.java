package com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy;

import com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.RemotingApiGrpc;
import io.grpc.ManagedChannel;

/**
 * @author baB_hyf
 * @date 2022/10/01
 */
public class ConnectionWrapper {

    private final String                                addr;
    private final ManagedChannel                        channel;
    private final RemotingApiGrpc.RemotingApiFutureStub futureStub;

    public ConnectionWrapper(String addr, ManagedChannel channel, RemotingApiGrpc.RemotingApiFutureStub futureStub) {
        this.addr = addr;
        this.channel = channel;
        this.futureStub = futureStub;
    }

    public String getAddr() {
        return addr;
    }

    public ManagedChannel getChannel() {
        return channel;
    }

    public RemotingApiGrpc.RemotingApiFutureStub getFutureStub() {
        return futureStub;
    }

    public void close() {
        if (channel != null && !channel.isShutdown()) {
            try {
                channel.shutdownNow();
            } catch (Throwable ignored) {
            }
        }
    }
}
