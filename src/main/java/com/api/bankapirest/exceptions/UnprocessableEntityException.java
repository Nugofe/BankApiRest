package com.api.bankapirest.exceptions;

import org.springframework.http.HttpStatus;

public class UnprocessableEntityException extends ApiException {

    public UnprocessableEntityException(String message) {
        super(message, HttpStatus.UNPROCESSABLE_ENTITY);
    }

}
