package com.hyf.cloudnative.remoting.mesh;

/**
 * Built-in request way, can be customized when needs to extend other {@link com.hyf.cloudnative.remoting.mesh.proxy.ProxyProvider}.
 *
 * @see com.hyf.cloudnative.remoting.mesh.proxy.ProxyProvider
 */
public class RequestWay {

    /**
     * http
     */
    public static final String HTTP = "http";

    /**
     * grpc
     */
    public static final String GRPC = "grpc";

}
