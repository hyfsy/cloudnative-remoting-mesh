package com.hyf.cloudnative.remoting.mesh.proxy;

import java.util.Objects;

/**
 * k8s client invocation context.
 *
 * @param <T> client type, depends on implements
 * @see AbstractRemotingInvocationHandler
 * @see com.hyf.cloudnative.remoting.mesh.proxy.http.HttpInvocationHandler
 * @see com.hyf.cloudnative.remoting.mesh.proxy.grpc.GrpcInvocationHandler
 */
public class InvocationContext<T> {

    /**
     * method invocation caller type, usually is a interface
     */
    private Class<?> type;

    /**
     * communicate client, depends on implements
     */
    private T client;

    /**
     * k8s service address
     */
    private String addr;

    public InvocationContext(Class<?> type, T client, String addr) {
        this.type = type;
        this.client = client;
        this.addr = addr;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }

    public T getClient() {
        return client;
    }

    public void setClient(T client) {
        this.client = client;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        InvocationContext<?> that = (InvocationContext<?>) o;
        return Objects.equals(type, that.type) && Objects.equals(client, that.client) && Objects.equals(addr, that.addr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, client, addr);
    }

    @Override
    public String toString() {
        return "Invocation[type=" + type + ", client=" + client + ", addr=" + addr + ']';
    }
}
