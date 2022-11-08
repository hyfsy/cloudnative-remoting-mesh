package com.hyf.cloudnative.remoting.mesh.proxy.grpc;

import com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.Message;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate.RemotingApiGrpc;
import com.hyf.cloudnative.remoting.mesh.proxy.grpc.utils.RemotingUtils;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author baB_hyf
 * @date 2022/10/01
 */
public class RemotingApiAcceptor extends RemotingApiGrpc.RemotingApiImplBase {

    public static final Logger log = LoggerFactory.getLogger(RemotingApiAcceptor.class);

    private final ServerHandler serverHandler;

    public RemotingApiAcceptor(ServerHandler serverHandler) {
        this.serverHandler = serverHandler;
    }

    @Override
    public void request(Message request, StreamObserver<Message> responseObserver) {

        // 事件处理
        if (request.getEvent()) {
            eventHandle(request, responseObserver);
            return;
        }

        try {
            com.hyf.cloudnative.remoting.mesh.proxy.grpc.Message message = RemotingUtils.convert(request);
            com.hyf.cloudnative.remoting.mesh.proxy.grpc.Message response = serverHandler.handle(message);
            if (response != null) {
                responseObserver.onNext(RemotingUtils.convert(response));
            }
            responseObserver.onCompleted();
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            log.error("Failed to handle the request", targetException);
            responseObserver.onError(targetException);
        } catch (Throwable e) {
            log.error("Failed to handle the request", e);
            responseObserver.onError(e);
        }
    }

    @Override
    public StreamObserver<Message> biStream(StreamObserver<Message> responseObserver) {
        return new StreamObserver<Message>() {
            @Override
            public void onNext(Message message) {
                request(message, responseObserver);
            }

            @Override
            public void onError(Throwable t) {
                log.error("Failed to handle the request", t);
                responseObserver.onError(t);
                responseObserver.onCompleted();
            }

            @Override
            public void onCompleted() {
                responseObserver.onCompleted();
            }
        };
    }

    private void eventHandle(Message request, StreamObserver<Message> responseObserver) {
        // heartbeat
        if (RemotingUtils.PING.getMetadataMap().get(RemotingUtils.HEALTH_CHECK_METADATA_K).equals(request.getMetadataMap().get(RemotingUtils.HEALTH_CHECK_METADATA_K))) {
            responseObserver.onNext(RemotingUtils.PONG);
            responseObserver.onCompleted();
        }
    }
}
