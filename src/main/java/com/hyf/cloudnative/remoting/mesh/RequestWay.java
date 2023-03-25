package com.hyf.cloudnative.remoting.mesh;

/**
 * built-in request way, can be customized when needs to extend other {@link com.hyf.cloudnative.remoting.mesh.proxy.ProxyProvider}.
 *
 * @see com.hyf.cloudnative.remoting.mesh.proxy.ProxyProvider
 */
public class RequestWay {

    public static final String HTTP = "http";

    public static final String GRPC = "grpc";

}
