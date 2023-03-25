package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

public class Message {
    private int                 id;
    private boolean             req   = true;
    private boolean             event = false;
    private Class<?>            clazz;
    private Method              method;
    private Object              body;
    private Map<String, String> metadata;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isReq() {
        return req;
    }

    public void setReq(boolean req) {
        this.req = req;
    }

    public boolean isEvent() {
        return event;
    }

    public void setEvent(boolean event) {
        this.event = event;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {
        this.body = body;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Message message = (Message) o;
        return id == message.id && req == message.req && event == message.event && Objects.equals(clazz, message.clazz) && Objects.equals(method, message.method) && Objects.equals(body, message.body) && Objects.equals(metadata, message.metadata);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, req, event, clazz, method, body, metadata);
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", req=" + req +
                ", event=" + event +
                ", clazz=" + clazz +
                ", method=" + method +
                ", body=" + body +
                ", metadata=" + metadata +
                '}';
    }
}
