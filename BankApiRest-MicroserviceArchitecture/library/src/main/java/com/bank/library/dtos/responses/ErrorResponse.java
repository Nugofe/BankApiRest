package com.bank.library.dtos.responses;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Component
public class ErrorResponse {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(type = "string", pattern = "yyyy-MM-dd HH:mm:ss", example = "2023-11-24 08:37:13")
    private LocalDateTime timestamp;

    @Schema(example = "BAD_REQUEST")
    private HttpStatus statusError;

    @Schema(example = "NIF field incorrect pattern (example: 12345678A)")
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

    public ResponseEntity<Object> generateResponseEntity() {
        return new ResponseEntity<>(this, this.statusError);
    }
}
