package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import com.hyf.cloudnative.remoting.mesh.proxy.grpc.utils.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * grpc invocation metadata registry, collect all instances/classes/methods metadata from
 * the interfaces of the bean annotated by {@link com.hyf.cloudnative.remoting.mesh.GrpcController}
 *
 * @see com.hyf.cloudnative.remoting.mesh.GrpcController
 */
public class InvocationMetadataRegistry {

    private static final Logger log = LoggerFactory.getLogger(InvocationMetadataRegistry.class);

    // className -> instance
    private static final Map<String, Object> instanceMap = new ConcurrentHashMap<>();

    // className -> class
    private static final Map<String, Class<?>> classMap = new ConcurrentHashMap<>();

    // className.methodName(argClassName) -> method
    private static final Map<String, Method> methodMap = new ConcurrentHashMap<>();

    /**
     * get the controller bean from cache
     *
     * @param clazz the controller bean type
     * @return the controller bean
     */
    public static Object getInstance(Class<?> clazz) {
        return instanceMap.get(clazz.getName());
    }

    /**
     * get the type of the controller bean from cache
     *
     * @param className the controller bean type class name
     * @return the controller bean type
     */
    public static Class<?> getClazz(String className) {
        return classMap.get(className);
    }

    /**
     * get the method of the controller bean from cache
     *
     * @param className    the class name of the controller bean type
     * @param methodName   the method name of the controller bean
     * @param argClassName the parameters string of the method of the controller bean, for example: "arg1,arg2"
     * @return the method of the controller bean
     */
    public static Method getMethod(String className, String methodName, String argClassName) {
        return methodMap.get(MethodUtils.generateMethodSignature(className, methodName, argClassName));
    }

    /**
     * register the metadata of the controller bean instance, include class/method
     *
     * @param clazz    the type of the controller bean
     * @param instance controller bean instance
     */
    public static void registerInstanceMetadata(Class<?> clazz, Object instance) {
        if (log.isDebugEnabled()) {
            log.debug("Registry instance: {}, mapper class: {}", instance, clazz.getName());
        }
        Object old = instanceMap.put(clazz.getName(), instance);
        if (old != null) {
            log.warn("Registry instance override, class: {}, old: {}, new: {}", clazz.getName(), old, instance);
        }
        registerClassMetadata(clazz);
    }

    /**
     * register the type metadata of the controller bean, include method
     *
     * @param clazz the type of the controller bean
     */
    public static void registerClassMetadata(Class<?> clazz) {
        if (log.isDebugEnabled()) {
            log.debug("Registry class: {}", clazz.getName());
        }
        Class<?> old = classMap.put(clazz.getName(), clazz);
        if (old != null) {
            log.warn("Registry class override, old: {}, new: {}", old.getName(), clazz.getName());
        }
        for (Method declaredMethod : clazz.getDeclaredMethods()) {
            registerMethodMetadata(clazz, declaredMethod);
        }
    }

    /**
     * register the metadata of the method
     *
     * @param clazz  the type of the controller bean
     * @param method the method of the controller bean
     */
    public static void registerMethodMetadata(Class<?> clazz, Method method) {
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
