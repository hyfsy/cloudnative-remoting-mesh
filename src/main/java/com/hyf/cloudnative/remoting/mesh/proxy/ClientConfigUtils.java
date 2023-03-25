package com.hyf.cloudnative.remoting.mesh.proxy;

import org.springframework.util.StringUtils;

import java.util.function.Consumer;

/**
 * convenient utility to operate client config.
 */
public class ClientConfigUtils {

    public static <T> void setIfNotNull(T o, Consumer<T> setAction) {
        if (o != null) {
            setAction.accept(o);
        }
    }

    public static void setIfPositive(Integer i, Consumer<Integer> setAction) {
        if (i != null && i > 0) {
            setAction.accept(i);
        }
    }

    public static void setIfHasText(String str, Consumer<String> setAction) {
        if (StringUtils.hasText(str)) {
            setAction.accept(str);
        }
    }

    public static <T> void setIfNotVoidClass(Class<T> clazz, Consumer<Class<T>> setAction) {
        if (clazz != null && clazz != void.class) {
            setAction.accept(clazz);
        }
    }
}
