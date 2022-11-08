package com.hyf.cloudnative.remoting.mesh.exception;

public class RemotingException extends Exception {

    public RemotingException(String message) {
        super(message);
    }

    public RemotingException(Throwable cause) {
        super(cause);
    }

    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }
}
