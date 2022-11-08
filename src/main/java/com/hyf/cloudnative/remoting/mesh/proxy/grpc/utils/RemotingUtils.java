package com.hyf.cloudnative.remoting.mesh.proxy.grpc.utils;

import com.hyf.cloudnative.remoting.mesh.proxy.grpc.InvocationMetadataRegistry;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.Message;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.JacksonUtils;
import com.hyf.cloudnative.remoting.mesh.exception.DeserializationException;
import com.fasterxml.jackson.databind.util.ByteBufferBackedInputStream;
import com.google.protobuf.Any;
import com.google.protobuf.ByteString;
import com.google.protobuf.UnsafeByteOperations;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

public abstract class RemotingUtils {

    public static final String HEALTH_CHECK_METADATA_K = "beat";
    public static final String HEALTH_CHECK_METADATA_V_PING = "ping";
    public static final String HEALTH_CHECK_METADATA_V_PONG = "pong";

    public static final com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message PING = com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message.newBuilder().setEvent(true).putMetadata(HEALTH_CHECK_METADATA_K, HEALTH_CHECK_METADATA_V_PING).build();
    public static final com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message PONG = com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message.newBuilder().setEvent(true).putMetadata(HEALTH_CHECK_METADATA_K, HEALTH_CHECK_METADATA_V_PONG).build();

    public static InetSocketAddress parseSocketAddress(String addr) {
        if (addr == null) {
            throw new IllegalArgumentException("addr is null");
        }

        String[] addrPair = addr.split(":");
        if (addrPair.length != 2) {
            throw new IllegalArgumentException("addr illegal: " + addr);
        }

        String host = addrPair[0];
        int port;
        try {
            port = Integer.parseInt(addrPair[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("addr illegal: " + addr, e);
        }

        return new InetSocketAddress(host, port);
    }

    public static Message convert(com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message message) {
        Message msg = new Message();
        msg.setId(message.getId());
        msg.setReq(message.getReq());
        msg.setEvent(message.getEvent());

        Class<?> clazz = InvocationMetadataRegistry.getClazz(message.getClassName().toStringUtf8());
        Assert.notNull(clazz, "Remoting message class not been registered: " + message.getClassName().toStringUtf8());
        Method method = InvocationMetadataRegistry.getMethod(clazz.getName(), message.getMethodName().toStringUtf8(), message.getArgClassName().toStringUtf8());
        Assert.notNull(clazz, "Remoting message method not been registered: " + message.getMethodName().toStringUtf8());
        msg.setClazz(clazz);
        msg.setMethod(method);

        String payloadClassName = message.getPayloadClassName().toStringUtf8();
        if (!StringUtils.hasText(payloadClassName)) {
            if (message.getReq()) {
                payloadClassName = MethodUtils.getFirstParameterTypeNameSafely(method);
            } else {
                payloadClassName = method.getReturnType().getName();
            }
        }

        if (StringUtils.hasText(payloadClassName)) {
            Any body = message.getPayload();
            if (body.getSerializedSize() != 0) {
                ByteBuffer byteBuffer = body.getValue().asReadOnlyByteBuffer();
                try {
                    msg.setBody(JacksonUtils.toObj(new ByteBufferBackedInputStream(byteBuffer), ClassUtils.forName(payloadClassName, null)));
                } catch (ClassNotFoundException e) {
                    throw new DeserializationException(e);
                }
            }
        }

        msg.setMetadata(message.getMetadataMap());

        return msg;
    }

    public static com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message convert(Message message) {
        com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message.Builder builder = com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message.newBuilder();
        builder.setId(message.getId());
        builder.setReq(message.isReq());
        builder.setEvent(message.isEvent());
        if (message.getClazz() != null) {
            builder.setClassName(ByteString.copyFromUtf8(message.getClazz().getName()));
        }
        if (message.getMethod() != null) {
            builder.setMethodName(ByteString.copyFromUtf8(message.getMethod().getName()));
        }
        if (message.getMethod() != null && message.getMethod().getParameterTypes().length == 1) {
            builder.setArgClassName(ByteString.copyFromUtf8(message.getMethod().getParameterTypes()[0].getName()));
        }
        if (message.getMethod() != null) {
            if (message.isReq()) {
                builder.setPayloadClassName(builder.getArgClassName());
            } else {
                builder.setPayloadClassName(ByteString.copyFromUtf8(message.getMethod().getReturnType().getName()));
            }
        }
        if (message.getBody() != null) {
            byte[] jsonBytes = JacksonUtils.toJsonBytes(message.getBody());
            builder.setPayload(Any.newBuilder().setValue(UnsafeByteOperations.unsafeWrap(jsonBytes)));
        }
        if (message.getMetadata() != null) {
            builder.putAllMetadata(message.getMetadata());
        }
        return builder.build();
    }
}
