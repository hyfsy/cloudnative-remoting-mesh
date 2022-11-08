package com.hyf.cloudnative.remoting.mesh.constants;

import com.hyf.cloudnative.remoting.mesh.RequestWay;

public class ClientConstants {

    public static final String PROPERTIES_PREFIX = "bcx.cloudnative.remoting";

    public static final RequestWay DEFAULT_REQUEST_WAY = RequestWay.GRPC;

    public static final int DEFAULT_HTTP_PORT = 8080;

    public static final int DEFAULT_HTTPS_PORT = 8443;

    public static final int DEFAULT_GRPC_PORT = 5443;

    public static final boolean DEFAULT_TLS_ENABLE = false;

    public static final String DEFAULT_NAMESPACE = "default";

    public static final String DEFAULT_CLUSTER_DOMAIN = "cluster.local";

}
