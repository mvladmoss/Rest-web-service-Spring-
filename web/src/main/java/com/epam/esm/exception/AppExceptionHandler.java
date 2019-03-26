package com.epam.esm.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AppExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppExceptionHandler.class);
    private static final String INCORRECT_FORMAT = "bad.type";
    private static final String NOT_READABLE_HTTP_MESSATE = "http.message.not.readable";
    private static final String HTTP_METHOD_NOT_SUPPORT = "not.support.http.method";
    private static final String UNSUPPORTED_MEDIA_TYPE = "unsupported.media.type";
    private static final String INTERNAL_SERVER_EXCEPTION = "server.internal.exception";

    private final MessageSource messageSource;

    @Autowired
    public AppExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
/*
    private ApiError getMessageWithArgs(String message, List<String> arguments) {
        String exceptionMessage = (String) messageSource.getMessage(message + MESSAGE);
        String messageWithArgument = MessageFormat.format(exceptionMessage, arguments);
        return new ApiError(messageWithArgument);
    }

    private ApiError getMessage(String message) {
        String exceptionMessage = (String) messageBundle.getObject(message + MESSAGE);
        return new ApiError(exceptionMessage);
    }*/

    /*@ExceptionHandler(NotInitializedConnectionPoolException.class)
    public ResponseEntity<ApiError> handleRuntimeException(NotInitializedConnectionPoolException exception) {
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(getMessage(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConnectionCloseException.class)
    public ResponseEntity<ApiError> handleRuntimeException(ConnectionCloseException exception) {
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(getMessage(exception.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleArgumentNotValidException(MethodArgumentNotValidException ex
            , Locale locale) {
        BindingResult result = ex.getBindingResult();
        List<String> errorMessages = result.getFieldErrors()
                .stream()
                .map(objectError -> messageSource.getMessage(objectError.getDefaultMessage()
                        , new Object[]{}, locale))
                .collect(Collectors.toList());
        return new ResponseEntity<>(new ApiError(errorMessages), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IncorrectSqlQueryParametersException.class})
    public ResponseEntity<ApiError> handleRuntimeException(IncorrectSqlQueryParametersException exception
            , Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getErrorKey(), exception.getArguments(), locale);
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(new ApiError(Collections.singletonList(errorMessage)),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public ResponseEntity<ApiError> handleRuntimeException(ObjectNotFoundException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getErrorKey(), exception.getArguments(), locale);
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(new ApiError(Collections.singletonList(errorMessage)), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NameAlreadyExistException.class)
    public ResponseEntity<ApiError> handleRuntimeException(NameAlreadyExistException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getErrorKey(), exception.getArguments(), locale);
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(new ApiError(Collections.singletonList(errorMessage)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleRuntimeException(Exception exception, Locale locale) {
        String errorMessage = messageSource.getMessage(INTERNAL_SERVER_EXCEPTION, new Object[]{}, locale);
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(new ApiError(Collections.singletonList(exception.getLocalizedMessage()))
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    public ResponseEntity<ApiError> handleRuntimeException(BadSqlGrammarException e, Locale locale) {
        String error = messageSource.getMessage(INCORRECT_FORMAT, new Object[]{}, locale);
        return new ResponseEntity<>(new ApiError(Collections.singletonList(error)), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiError> handleRuntimeException(HttpMessageNotReadableException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(NOT_READABLE_HTTP_MESSATE, new Object[]{}, locale);
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(new ApiError(Collections.singletonList(errorMessage)), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleRuntimeException(HttpRequestMethodNotSupportedException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(HTTP_METHOD_NOT_SUPPORT, new Object[]{}, locale);
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(new ApiError(Collections.singletonList(errorMessage))
                , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ApiError> handleRuntimeException(HttpMediaTypeNotSupportedException exception, Locale locale) {
        String errorMessage = messageSource.getMessage(UNSUPPORTED_MEDIA_TYPE, new Object[]{}, locale);
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(new ApiError(Collections.singletonList(errorMessage)), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NotInitializedConnectionPoolException.class)
    public ResponseEntity<ApiError> handleRuntimeException(NotInitializedConnectionPoolException exception,
                                                           Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getErrorKey(), new Object[]{}, locale);
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(new ApiError(Collections.singletonList(errorMessage))
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConnectionCloseException.class)
    public ResponseEntity<ApiError> handleRuntimeException(ConnectionCloseException exception,
                                                           Locale locale) {
        String errorMessage = messageSource.getMessage(exception.getKeyError(), new Object[]{}, locale);
        LOGGER.error(exception.getMessage());
        return new ResponseEntity<>(new ApiError(Collections.singletonList(errorMessage))
                , HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
