package com.rage.ecommerce.application.dto.order;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ErrorResponseDTO {
    private String message;
    private int status;
    private String error;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ErrorResponseDTO(String message) {
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(String message, int status, String error) {
        this.message = message;
        this.status = status;
        this.error = error;
        this.timestamp = LocalDateTime.now();
    }
}
