package com.hyf.cloudnative.remoting.mesh.proxy;

import java.util.Objects;

public class InvocationContext<T> {
    private Class<?> type;
    private T client;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
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
