package com.rage.ecommerce.domain.exception;

import com.rage.ecommerce.application.dto.order.ErrorResponseDTO;
import com.rage.ecommerce.infrastructure.exception.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponseDTO> handleResourceNotFoundException(ResourceNotFoundException ex) {
        logger.error("Resource not found exception: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                "Not Found"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponseDTO> handleBadRequestException(BadRequestException ex) {
        logger.error("Bad request exception: {}", ex.getMessage());
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                ex.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                "Bad Request"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleServiceException(ServiceException ex) {
        logger.error("Service exception: {}", ex.getMessage(), ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "Failed to process your request. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.error("Validation exception: {}", ex.getMessage());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        logger.error("Data integrity violation occurred: ", ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "Data integrity violation occurred. Please check your input.",
                HttpStatus.CONFLICT.value(),
                "Data Conflict"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(Exception ex) {
        logger.error("Unhandled exception occurred: ", ex);
        ErrorResponseDTO errorResponse = new ErrorResponseDTO(
                "An unexpected error occurred. Please try again later.",
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error"
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
