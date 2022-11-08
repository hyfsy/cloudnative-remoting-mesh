package com.hyf.cloudnative.remoting.mesh.proxy;

import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public abstract class AbstractRemotingInvocationHandler<T> implements InvocationHandler {

    private final InvocationContext<T> context;

    public AbstractRemotingInvocationHandler(InvocationContext<T> context) {
        Assert.notNull(context, "InvocationContext is null");
        this.context = context;
    }

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

    protected abstract Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable;

    public InvocationContext<T> getContext() {
        return context;
    }
}
