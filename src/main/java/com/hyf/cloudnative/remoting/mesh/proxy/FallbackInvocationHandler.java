package com.hyf.cloudnative.remoting.mesh.proxy;

import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * wrap original {@link InvocationHandler}, supply fallback implementation.
 *
 * @see AbstractFallbackProxyProvider
 */
public class FallbackInvocationHandler implements InvocationHandler {

    /**
     * original proxy implementation
     */
    private final InvocationHandler delegate;

    /**
     * fallback instance factory
     */
    // @Nullable
    private final FallbackFactory<?> fallbackFactory;

    public FallbackInvocationHandler(InvocationHandler delegate, FallbackFactory<?> fallbackFactory) {
        this.delegate = delegate;
        this.fallbackFactory = fallbackFactory;
    }

    /**
     * handle exception when delegate invoke failed.
     *
     * @param proxy  client proxy instance
     * @param method client invoked method
     * @param args   method parameters
     * @return client response or fallback response
     * @throws Throwable fallback handle failed
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        Object result = null;
        try {
            method.setAccessible(true);
            result = delegate.invoke(proxy, method, args);
        } catch (Exception t) {
            if (fallbackFactory == null) {
                throw t;
            }

            try {
                Object fallbackInstance = fallbackFactory.create(t);
                result = ReflectionUtils.invokeMethod(method, fallbackInstance, args);
            } catch (Exception fallbackEx) {
                unwrapAndRethrow(fallbackEx);
            }
        }

        return result;
    }

    private void unwrapAndRethrow(Exception exception) {
        if (exception instanceof InvocationTargetException) { //  || exception instanceof NoFallbackAvailableException
            Throwable underlyingException = exception.getCause();
            if (underlyingException instanceof RuntimeException) {
                throw (RuntimeException) underlyingException;
            }
            if (underlyingException != null) {
                throw new IllegalStateException(underlyingException);
            }
            throw new IllegalStateException(exception);
        }
    }
}
