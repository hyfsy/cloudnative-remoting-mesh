package com.hyf.cloudnative.remoting.mesh.proxy.grpc.generate;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ClientCalls.asyncClientStreamingCall;
import static io.grpc.stub.ClientCalls.asyncServerStreamingCall;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingServerStreamingCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncBidiStreamingCall;
import static io.grpc.stub.ServerCalls.asyncClientStreamingCall;
import static io.grpc.stub.ServerCalls.asyncServerStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedStreamingCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.9.1)",
    comments = "Source: remoting_api.proto")
public final class RemotingApiGrpc {

  private RemotingApiGrpc() {}

  public static final String SERVICE_NAME = "remoting.v1.RemotingApi";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @Deprecated // Use {@link #getRequestMethod()} instead.
  public static final io.grpc.MethodDescriptor<Message,
      Message> METHOD_REQUEST = getRequestMethod();

  private static volatile io.grpc.MethodDescriptor<Message,
      Message> getRequestMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<Message,
      Message> getRequestMethod() {
    io.grpc.MethodDescriptor<Message, Message> getRequestMethod;
    if ((getRequestMethod = RemotingApiGrpc.getRequestMethod) == null) {
      synchronized (RemotingApiGrpc.class) {
        if ((getRequestMethod = RemotingApiGrpc.getRequestMethod) == null) {
          RemotingApiGrpc.getRequestMethod = getRequestMethod = 
              io.grpc.MethodDescriptor.<Message, Message>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "remoting.v1.RemotingApi", "Request"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Message.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Message.getDefaultInstance()))
                  .setSchemaDescriptor(new RemotingApiMethodDescriptorSupplier("Request"))
                  .build();
          }
        }
     }
     return getRequestMethod;
  }
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  @Deprecated // Use {@link #getBiStreamMethod()} instead.
  public static final io.grpc.MethodDescriptor<Message,
      Message> METHOD_BI_STREAM = getBiStreamMethod();

  private static volatile io.grpc.MethodDescriptor<Message,
      Message> getBiStreamMethod;

  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static io.grpc.MethodDescriptor<Message,
      Message> getBiStreamMethod() {
    io.grpc.MethodDescriptor<Message, Message> getBiStreamMethod;
    if ((getBiStreamMethod = RemotingApiGrpc.getBiStreamMethod) == null) {
      synchronized (RemotingApiGrpc.class) {
        if ((getBiStreamMethod = RemotingApiGrpc.getBiStreamMethod) == null) {
          RemotingApiGrpc.getBiStreamMethod = getBiStreamMethod = 
              io.grpc.MethodDescriptor.<Message, Message>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.BIDI_STREAMING)
              .setFullMethodName(generateFullMethodName(
                  "remoting.v1.RemotingApi", "BiStream"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Message.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  Message.getDefaultInstance()))
                  .setSchemaDescriptor(new RemotingApiMethodDescriptorSupplier("BiStream"))
                  .build();
          }
        }
     }
     return getBiStreamMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static RemotingApiStub newStub(io.grpc.Channel channel) {
    return new RemotingApiStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static RemotingApiBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new RemotingApiBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static RemotingApiFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new RemotingApiFutureStub(channel);
  }

  /**
   */
  public static abstract class RemotingApiImplBase implements io.grpc.BindableService {

    /**
     */
    public void request(Message request,
                        io.grpc.stub.StreamObserver<Message> responseObserver) {
      asyncUnimplementedUnaryCall(getRequestMethod(), responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<Message> biStream(
        io.grpc.stub.StreamObserver<Message> responseObserver) {
      return asyncUnimplementedStreamingCall(getBiStreamMethod(), responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getRequestMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                Message,
                Message>(
                  this, METHODID_REQUEST)))
          .addMethod(
            getBiStreamMethod(),
            asyncBidiStreamingCall(
              new MethodHandlers<
                Message,
                Message>(
                  this, METHODID_BI_STREAM)))
          .build();
    }
  }

  /**
   */
  public static final class RemotingApiStub extends io.grpc.stub.AbstractStub<RemotingApiStub> {
    private RemotingApiStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RemotingApiStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RemotingApiStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RemotingApiStub(channel, callOptions);
    }

    /**
     */
    public void request(Message request,
                        io.grpc.stub.StreamObserver<Message> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getRequestMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public io.grpc.stub.StreamObserver<Message> biStream(
        io.grpc.stub.StreamObserver<Message> responseObserver) {
      return asyncBidiStreamingCall(
          getChannel().newCall(getBiStreamMethod(), getCallOptions()), responseObserver);
    }
  }

  /**
   */
  public static final class RemotingApiBlockingStub extends io.grpc.stub.AbstractStub<RemotingApiBlockingStub> {
    private RemotingApiBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RemotingApiBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RemotingApiBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RemotingApiBlockingStub(channel, callOptions);
    }

    /**
     */
    public Message request(Message request) {
      return blockingUnaryCall(
          getChannel(), getRequestMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class RemotingApiFutureStub extends io.grpc.stub.AbstractStub<RemotingApiFutureStub> {
    private RemotingApiFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private RemotingApiFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected RemotingApiFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new RemotingApiFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<Message> request(
        Message request) {
      return futureUnaryCall(
          getChannel().newCall(getRequestMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_REQUEST = 0;
  private static final int METHODID_BI_STREAM = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final RemotingApiImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(RemotingApiImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_REQUEST:
          serviceImpl.request((Message) request,
              (io.grpc.stub.StreamObserver<Message>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_BI_STREAM:
          return (io.grpc.stub.StreamObserver<Req>) serviceImpl.biStream(
              (io.grpc.stub.StreamObserver<Message>) responseObserver);
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class RemotingApiBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    RemotingApiBaseDescriptorSupplier() {}

    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return RemotingApiProto.getDescriptor();
    }

    @Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("RemotingApi");
    }
  }

  private static final class RemotingApiFileDescriptorSupplier
      extends RemotingApiBaseDescriptorSupplier {
    RemotingApiFileDescriptorSupplier() {}
  }

  private static final class RemotingApiMethodDescriptorSupplier
      extends RemotingApiBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    RemotingApiMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (RemotingApiGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new RemotingApiFileDescriptorSupplier())
              .addMethod(getRequestMethod())
              .addMethod(getBiStreamMethod())
              .build();
        }
      }
    }
    return result;
  }
}
