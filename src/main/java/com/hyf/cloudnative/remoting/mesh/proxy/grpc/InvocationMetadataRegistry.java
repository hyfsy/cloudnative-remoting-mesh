package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import com.hyf.cloudnative.remoting.mesh.proxy.grpc.utils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InvocationMetadataRegistry {

    private static final Logger log = LoggerFactory.getLogger(InvocationMetadataRegistry.class);

    // className -> instance
    private static final Map<String, Object> instanceMap = new ConcurrentHashMap<>();

    // className -> class
    private static final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    // className.methodName(argClassName) -> method
    private static final Map<String, Method> methodMap = new ConcurrentHashMap<>();

    public static Object getInstance(Class<?> clazz) {
        return instanceMap.get(clazz.getName());
    }

    public static Class<?> getClazz(String className) {
        return classMap.get(className);
    }

    public static Method getMethod(String className, String methodName, String argClassName) {
        return methodMap.get(MethodUtils.generateMethodSignature(className, methodName, argClassName));
    }

    public static void registryInstance(Class<?> clazz, Object instance) {
        if (log.isDebugEnabled()) {
            log.debug("Registry instance: {}, mapper class: {}", instance, clazz.getName());
        }
        Object old = instanceMap.put(clazz.getName(), instance);
        if (old != null) {
            log.warn("Registry instance override, class: {}, old: {}, new: {}", clazz.getName(), old, instance);
        }
        registryClass(clazz);
    }

    public static void registryClass(Class<?> clazz) {
        if (log.isDebugEnabled()) {
            log.debug("Registry class: {}", clazz.getName());
        }
        Class<?> old = classMap.put(clazz.getName(), clazz);
        if (old != null) {
            log.warn("Registry class override, old: {}, new: {}", old.getName(), clazz.getName());
        }
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            registryMethod(clazz, declaredMethod);
        }
    }

    public static void registryMethod(Class<?> clazz, Method method) {
        if (log.isDebugEnabled()) {
            log.debug("Registry method: {}", clazz.getName() + "." + method.getName());
        }
        if (!MethodUtils.supportMethodForGrpc(method)) {
            throw new IllegalStateException("Registry method only support 0 or 1 parameter: " + clazz.getName() + "." + method.getName());
        }
        String firstParameterTypeName = MethodUtils.getFirstParameterTypeNameSafely(method);
        String methodSignature = MethodUtils.generateMethodSignature(clazz.getName(), method.getName(), firstParameterTypeName);
        Method old = methodMap.put(methodSignature, method);
        if (old != null) {
            log.warn("Registry method override, class: {}, old: {}, new: {}", clazz.getName(), old.getName(), method.getName());
        }
    }
}
