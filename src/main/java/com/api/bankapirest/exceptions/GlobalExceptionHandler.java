package com.api.bankapirest.exceptions;

import com.api.bankapirest.dtos.response.ErrorResponse;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // VALIDATION
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        List<String> errors = new ArrayList<>();
        ex.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));
        logger.error("MethodArgumentNotValidException: {}", errors, ex);

        ErrorResponse response = new ErrorResponse(errors, HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, headers, response.getStatusError());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.error("HttpMessageNotReadableException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(response, headers, response.getStatusError());
    }

    // AUTHENTICATION
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> notValid(AuthenticationException ex) {
        logger.error("AuthenticationException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
        return response.generateResponseEntity();
    }

    // API EXCEPTIONS
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> notValid(ApiException ex) {
        logger.error("ApiException: {}", ex.getMessage(), ex);
        return ex.getResponse().generateResponseEntity();
    }

    // SERVICE UNAVAILABLE
    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<?> notValid(CallNotPermittedException ex) {
        logger.error("CallNotPermittedException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        return response.generateResponseEntity();
    }

    @ExceptionHandler(ResourceAccessException.class)
    public ResponseEntity<?> notValid(ResourceAccessException ex) {
        logger.error("ResourceAccessException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse("Service Unavailable", HttpStatus.SERVICE_UNAVAILABLE);
        return response.generateResponseEntity();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> notValid(Exception ex) {
        logger.error("Exception: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse("UNEXPECTED ERROR:  " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return response.generateResponseEntity();
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.error("HttpRequestMethodNotSupportedException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.METHOD_NOT_ALLOWED);
        return new ResponseEntity<>(response, headers, response.getStatusError());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.error("HttpMediaTypeNotSupportedException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        return new ResponseEntity<>(response, headers, response.getStatusError());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.error("HttpMediaTypeNotAcceptableException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        return new ResponseEntity<>(response, headers, response.getStatusError());
    }

    @Override
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            AsyncRequestTimeoutException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.error("AsyncRequestTimeoutException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.REQUEST_TIMEOUT);
        return new ResponseEntity<>(response, headers, response.getStatusError());
    }

    @Override
    protected ResponseEntity<Object> handleConversionNotSupported(
            ConversionNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        logger.error("ConversionNotSupportedException: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_IMPLEMENTED);
        return new ResponseEntity<>(response, headers, response.getStatusError());
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request
    ) {
        logger.error("Exception: {}", ex.getMessage(), ex);
        ErrorResponse response = new ErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, headers, response.getStatusError());
    }
}
