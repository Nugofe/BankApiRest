package com.api.bankapirest.exceptions;

import com.api.bankapirest.dtos.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    // VALIDATION
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> notValid(MethodArgumentNotValidException e, HttpServletRequest request) {
        List<String> errors = new ArrayList<>();
        e.getAllErrors().forEach(err -> errors.add(err.getDefaultMessage()));
        return new ErrorResponse(errors, HttpStatus.BAD_REQUEST).generateResponseEntity();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> notValid(HttpMessageNotReadableException e, HttpServletRequest request) {
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST).generateResponseEntity();
    }

    // AUTHENTICATION
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> notValid(AuthenticationException e, HttpServletRequest request) {
        return new ErrorResponse("Authentication unsuccessful", HttpStatus.UNAUTHORIZED).generateResponseEntity();
    }

    // API EXCEPTIONS
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> notValid(ApiException e, HttpServletRequest request) {
        return e.getResponse().generateResponseEntity();
    }
}
