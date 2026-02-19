package com.budgettracker.api.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetail> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.builder()
                .type("about:blank")
                .title("Resource Not Found")
                .status(HttpStatus.NOT_FOUND.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<ProblemDetail> handleDuplicateResourceException(
            DuplicateResourceException ex, HttpServletRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.builder()
                .type("about:blank")
                .title("Duplicate Resource")
                .status(HttpStatus.CONFLICT.value())
                .detail(ex.getMessage())
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        
        List<ProblemDetail.ValidationError> errors = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .map(error -> {
                    String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
                    String message = error.getDefaultMessage();
                    Object rejectedValue = error instanceof FieldError ? ((FieldError) error).getRejectedValue() : null;
                    
                    return ProblemDetail.ValidationError.builder()
                            .field(fieldName)
                            .message(message)
                            .rejectedValue(rejectedValue)
                            .build();
                })
                .collect(Collectors.toList());
        
        ProblemDetail problemDetail = ProblemDetail.builder()
                .type("about:blank")
                .title("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .detail("One or more fields have validation errors")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .errors(errors)
                .build();
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGenericException(
            Exception ex, HttpServletRequest request) {
        
        ProblemDetail problemDetail = ProblemDetail.builder()
                .type("about:blank")
                .title("Internal Server Error")
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .detail("An unexpected error occurred")
                .instance(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
    }
}
