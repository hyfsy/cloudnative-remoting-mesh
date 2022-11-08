package com.hyf.cloudnative.remoting.mesh.proxy;

public interface FallbackFactory<T> {

    T create(Throwable cause);

    final class Default<T> implements FallbackFactory<T> {

        final T constant;

        public Default(T constant) {
            this.constant = constant;
        }

        @Override
        public T create(Throwable cause) {
            return constant;
        }

        @Override
        public String toString() {
            return constant.toString();
        }
    }
}
