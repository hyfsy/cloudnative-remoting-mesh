package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import com.hyf.cloudnative.remoting.mesh.exception.RemotingException;

import java.lang.reflect.Method;

public class ServerHandler {

    public Message handle(Message message) throws Exception {
        Object instance = InvocationMetadataRegistry.getInstance(message.getClazz());
        if (instance == null) {
            throw new RemotingException("Instance is null, class: " + message.getClazz());
        }

        Method method = message.getMethod();
        method.setAccessible(true);
        Object rtn;
        if (message.getBody() == null) {
            rtn = method.invoke(instance);
        }
        else {
            rtn = method.invoke(instance, message.getBody());
        }

        Message rtnMsg = new Message();
        rtnMsg.setId(message.getId());
        rtnMsg.setReq(false);
        rtnMsg.setEvent(false);
        rtnMsg.setClazz(message.getClazz());
        rtnMsg.setMethod(message.getMethod());
        rtnMsg.setBody(rtn);

        return rtnMsg;
    }
}
