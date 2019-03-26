package com.epam.esm.exception;


import java.util.ArrayList;
import java.util.List;

public class ObjectNotFoundException extends RuntimeException {

    private String errorKey;

    private final List<String> arguments = new ArrayList<>();

    public ObjectNotFoundException(String message) {
        super(message);
    }

    public ObjectNotFoundException(String message,String errorKey, Object... args) {
        super(message);
        this.errorKey = errorKey;
        collectArgs(args);
    }

    public ObjectNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    private void collectArgs(Object... args) {
        for (Object arg : args) {
            arguments.add(String.valueOf(arg));
        }
    }

    public String getErrorKey() {
        return errorKey;
    }

    public Object[] getArguments() {
        return arguments.toArray();
    }

}

