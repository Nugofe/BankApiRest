package com.bank.library.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException {

    public NotFoundException(String type) {
        super(type + " not found", HttpStatus.NOT_FOUND);
    }

}
