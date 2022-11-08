package com.hyf.cloudnative.remoting.mesh.proxy.grpc.utils;

import org.springframework.util.StringUtils;

import java.lang.reflect.Method;

public abstract class MethodUtils {

    public static String generateMethodSignature(String className, String methodName, String argClassName) {
        if (!StringUtils.hasText(methodName)) {
            throw new IllegalArgumentException("Method is null");
        }
        StringBuilder sb = new StringBuilder();
        if (StringUtils.hasText(className)) {
            sb.append(className).append(".");
        }
        sb.append(methodName);
        sb.append("(");
        if (StringUtils.hasText(argClassName)) {
            sb.append(argClassName);
        }
        sb.append(")");
        return sb.toString();
    }

    public static boolean supportMethodForGrpc(Method method) {
        return method.getParameterCount() == 0 || method.getParameterCount() == 1;
    }

    public static Class<?> getFirstParameterTypeSafely(Method method) {
        if (method.getParameterCount() <= 0) {
            return null;
        }
        return method.getParameterTypes()[0];
    }

    public static String getFirstParameterTypeNameSafely(Method method) {
        Class<?> firstParameterTypeSafely = getFirstParameterTypeSafely(method);
        if (firstParameterTypeSafely == null) {
            return null;
        }
        return firstParameterTypeSafely.getName();
    }
}
