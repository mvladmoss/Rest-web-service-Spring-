package com.epam.esm.exception;

import java.util.ArrayList;
import java.util.List;

public class NameAlreadyExistException extends RuntimeException {

    private String errorKey;

    private final List<String> arguments = new ArrayList<>();


    public NameAlreadyExistException(String message) {
        super(message);
    }

    public NameAlreadyExistException(String message,String errorKey, Object... args) {
        super(message);
        this.errorKey = errorKey;
        collectArgs(args);
    }

    public NameAlreadyExistException(String message, Throwable cause) {
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
