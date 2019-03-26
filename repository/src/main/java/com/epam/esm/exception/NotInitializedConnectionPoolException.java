package com.epam.esm.exception;

public class NotInitializedConnectionPoolException extends RuntimeException {


    private static final String CONNECTION_POOL_INITIALIZE_EXCEPTION = "connection_pool.initialize_error";

    public NotInitializedConnectionPoolException(String message) {
        super(message);
    }

    public NotInitializedConnectionPoolException(Throwable throwable){
        super(throwable);
    }

    public NotInitializedConnectionPoolException(String message, Throwable th) {
        super(message, th);
    }

    public String getErrorKey() {
        return CONNECTION_POOL_INITIALIZE_EXCEPTION;
    }
}
