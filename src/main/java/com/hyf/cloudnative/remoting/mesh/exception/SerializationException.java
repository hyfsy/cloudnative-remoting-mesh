package com.hyf.cloudnative.remoting.mesh.exception;

import java.lang.reflect.Type;

public class SerializationException extends RuntimeException {

    public SerializationException(Throwable throwable) {
        super(throwable);
    }

    public SerializationException(Class<?> targetClass, Throwable throwable) {
        super(throwable);
    }

    public SerializationException(Type targetType, Throwable throwable) {
        super(throwable);
    }
}
