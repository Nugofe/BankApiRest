package com.api.bankapirest.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
//@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;
    private HttpStatus statusError;
    private List<String> errors;

    public ErrorResponse(String message, HttpStatus status) {
        statusError = status;
        errors = new ArrayList<>();
        errors.add(message);
        timestamp = LocalDateTime.now();
    }

    public ErrorResponse(List<String> errors, HttpStatus status) {
        statusError = status;
        this.errors = errors;
        timestamp = LocalDateTime.now();
    }

    public ResponseEntity<ErrorResponse> generateResponseEntity() {
        return new ResponseEntity<>(this, this.statusError);
    }
}
