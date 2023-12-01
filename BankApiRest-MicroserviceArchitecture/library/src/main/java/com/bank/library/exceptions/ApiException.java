package com.bank.library.exceptions;

import com.bank.library.dtos.responses.ErrorResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.HttpStatus;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ApiException extends Exception {

    private ErrorResponse response;

    public ApiException(String message, HttpStatus status) {
        super(message);
        this.response = new ErrorResponse(message, status);
    }

    public ApiException(List<String> messages, HttpStatus status) {
        super(messages.toString());
        this.response = new ErrorResponse(messages, status);
    }
}
