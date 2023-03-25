package com.hyf.cloudnative.remoting.mesh.proxy;

/**
 * fallback factory interface, used to support service degradation.
 *
 * @param <T> client interface type
 */
public interface FallbackFactory<T> {

    /**
     * create a fallback instance.
     *
     * @param cause exception
     * @return a fallback instance
     */
    T create(Throwable cause);

    /**
     * default fallback implementation, return the specified constant.
     *
     * @param <T> client interface type
     */
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
