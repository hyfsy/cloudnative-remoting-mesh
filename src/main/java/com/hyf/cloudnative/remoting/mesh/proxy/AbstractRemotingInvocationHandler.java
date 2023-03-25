package com.hyf.cloudnative.remoting.mesh.proxy;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * proxy client instances, based on {@link InvocationContext} to make different network calls.
 * also used to deal default invocation, like equals/hashcode/toString method,
 * users are recommended to extend this.
 *
 * @param <T> client instance type in {@link InvocationContext}
 * @see InvocationContext
 */
public abstract class AbstractRemotingInvocationHandler<T> implements InvocationHandler {

    /**
     * client invocation context
     */
    private final InvocationContext<T> context;

    public AbstractRemotingInvocationHandler(InvocationContext<T> context) {
        Assert.notNull(context, "InvocationContext is null");
        this.context = context;
    }

    /**
     * deal default invocation, like equals/hashcode/toString method.
     *
     * @param proxy  client proxy instance
     * @param method proxy client method
     * @param args   proxy client method argument
     * @return network call response
     * @throws Throwable network call exception
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        if (ReflectionUtils.isEqualsMethod(method)) {
            return context.equals(args[0]);
        }

        if (ReflectionUtils.isHashCodeMethod(method)) {
            return context.hashCode();
        }
        if (ReflectionUtils.isToStringMethod(method)) {
            return context.toString();
        }

        return doInvoke(proxy, method, args);
    }

    /**
     * make network calls.
     *
     * @param proxy  client proxy instance
     * @param method proxy client method
     * @param args   proxy client method argument
     * @return network call response
     * @throws Throwable network call exception
     */
    protected abstract Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable;

    /**
     * get client context.
     *
     * @return client invocation context
     */
    public InvocationContext<T> getContext() {
        return context;
    }
}
