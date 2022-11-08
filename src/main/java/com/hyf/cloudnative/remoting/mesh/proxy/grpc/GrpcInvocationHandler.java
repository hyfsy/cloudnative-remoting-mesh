package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import com.hyf.cloudnative.remoting.mesh.proxy.InvocationContext;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.copy.client.GrpcClient;
import com.hyf.cloudnative.remoting.mesh.proxy.AbstractRemotingInvocationHandler;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.utils.MethodUtils;
import com.hyf.cloudnative.remoting.mesh.utils.ApplicationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

public class GrpcInvocationHandler extends AbstractRemotingInvocationHandler<GrpcClient> {

    private static final Logger log = LoggerFactory.getLogger(GrpcInvocationHandler.class);

    private static final AtomicInteger idx = new AtomicInteger(0);

    public GrpcInvocationHandler(InvocationContext<GrpcClient> invocationContext) {
        super(invocationContext);
    }

    @Override
    public Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {

        InvocationContext<GrpcClient> context = getContext();

        if (!MethodUtils.supportMethodForGrpc(method)) {
            throw new IllegalStateException("Method only support 0 or 1 parameter: " + getContext().getType().getName() + "." + method.getName());
        }

        Message message = new Message();
        message.setId(idx.incrementAndGet());
        message.setReq(true);
        message.setEvent(false);
        message.setClazz(context.getType());
        message.setMethod(method);
        message.setBody(args != null && args.length == 1 ? args[0] : null);

        ApplicationUtils.getOrderedList(MessageInterceptor.class)
                .forEach(i -> i.process(message, proxy, method, args, getContext()));

        if (log.isDebugEnabled()) {
            log.debug("Request address: {}, message: {}", context.getAddr(), message);
        }

        Message response = context.getClient().request(context.getAddr(), message);

        return response.getBody();
    }
}
