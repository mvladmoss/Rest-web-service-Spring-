package com.epam.esm.exception;

public class ConnectionCloseException extends RuntimeException {

    private static final String CONNECTION_CLOSE_EXCEPTION = "connection_pool.connection.close";

    public ConnectionCloseException(String message) {
        super(message);
    }

    public ConnectionCloseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionCloseException(Throwable cause) {
        super(cause);
    }

    public String getKeyError(){
        return CONNECTION_CLOSE_EXCEPTION;
    }
}
