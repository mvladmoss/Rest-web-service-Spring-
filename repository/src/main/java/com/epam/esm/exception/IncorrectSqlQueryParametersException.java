package com.epam.esm.exception;

import java.util.ArrayList;
import java.util.List;

public class IncorrectSqlQueryParametersException extends RuntimeException {

    private final List<String> arguments = new ArrayList<>();
    private String errorKey;

    public IncorrectSqlQueryParametersException(String message) {
        super(message);
    }

    public IncorrectSqlQueryParametersException(Throwable throwable) {
        super(throwable);
    }

    public IncorrectSqlQueryParametersException(String message, String errorKey, Object... args) {
        super(message);
        this.errorKey = errorKey;
        collectArgs(args);
    }

    public IncorrectSqlQueryParametersException(String message, Throwable cause) {
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