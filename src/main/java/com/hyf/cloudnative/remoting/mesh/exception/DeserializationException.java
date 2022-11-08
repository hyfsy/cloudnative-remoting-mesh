package com.hyf.cloudnative.remoting.mesh.exception;

import java.lang.reflect.Type;

public class DeserializationException extends RuntimeException {

    public DeserializationException(Throwable throwable) {
        super(throwable);
    }

    public DeserializationException(Class<?> targetClass, Throwable throwable) {
        super(throwable);
    }

    public DeserializationException(Type targetType, Throwable throwable) {
        super(throwable);
    }
}
