package com.hyf.cloudnative.remoting.mesh;

public enum RequestWay {

    HTTP("http"),
    GRPC("grpc"),
    ;

    private final String name;

    RequestWay(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
