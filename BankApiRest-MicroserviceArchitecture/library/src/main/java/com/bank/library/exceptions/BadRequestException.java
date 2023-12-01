package com.bank.library.exceptions;

import org.springframework.http.HttpStatus;

import java.util.List;

public class BadRequestException extends ApiException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }

    public BadRequestException(List<String> messages) {
        super(messages, HttpStatus.BAD_REQUEST);
    }
}
