package com.photowebsite.exception;

import com.photowebsite.dto.response.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
            ResourceNotFoundException ex, HttpServletRequest request) {
        return createErrorResponse(
            HttpStatus.NOT_FOUND, 
            "Resource Not Found", 
            ex.getMessage(), 
            request.getRequestURI()
        );
    }

    @ExceptionHandler(PhotoException.class)
    public ResponseEntity<ErrorResponse> handlePhotoException(
            PhotoException ex, HttpServletRequest request) {
        return createErrorResponse(
            HttpStatus.BAD_REQUEST, 
            "Photo Error", 
            ex.getMessage(), 
            request.getRequestURI()
        );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException ex, HttpServletRequest request) {
        return createErrorResponse(
            HttpStatus.BAD_REQUEST, 
            "File Size Error", 
            "File size exceeds maximum limit", 
            request.getRequestURI()
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .reduce("", (a, b) -> a + "; " + b);
            
        return createErrorResponse(
            HttpStatus.BAD_REQUEST, 
            "Validation Error", 
            errorMessage, 
            request.getRequestURI()
        );
    }

    private ResponseEntity<ErrorResponse> createErrorResponse(
            HttpStatus status, String error, String message, String path) {
        ErrorResponse errorResponse = new ErrorResponse(
            LocalDateTime.now(),
            status.value(),
            error,
            message,
            path
        );
        return new ResponseEntity<>(errorResponse, status);
    }
} 