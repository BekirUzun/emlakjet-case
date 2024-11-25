package com.emlakjet.ecommerce.config;

import com.emlakjet.ecommerce.dto.CommonResponse;
import com.emlakjet.ecommerce.exception.ServiceException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse> handleValidationExceptions(MethodArgumentNotValidException exception) {
        log.error("Validation exception occurred.", exception);
        var errors = exception.getBindingResult().getAllErrors().stream().map(error -> {
            if (error instanceof FieldError fieldError) {
                return String.format("FieldError: %s %s", fieldError.getField(), fieldError.getDefaultMessage());
            }
            return String.format("Error: %s", error.getDefaultMessage());
        }).collect(Collectors.joining(", "));

        log.error("Parsed errors: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse(errors));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<CommonResponse> handleServiceExceptions(HttpMessageNotReadableException exception) {
        log.error("HttpMessageNotReadableException occurred.", exception);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CommonResponse(exception.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<CommonResponse> handleServiceExceptions(ExpiredJwtException exception) {
        log.error("ExpiredJwtException occurred.", exception);
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new CommonResponse(exception.getMessage()));
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<CommonResponse> handleServiceExceptions(ServiceException exception) {
        log.error("ServiceException occurred.", exception);
        return ResponseEntity
                .status(exception.getHttpStatus())
                .body(new CommonResponse(exception.getMessage()));
    }

    @ExceptionHandler(CallNotPermittedException.class)
    public ResponseEntity<CommonResponse> handleCircuitBreakerExceptions(CallNotPermittedException exception) {
        log.error("A CallNotPermittedException exception occurred.", exception);
        var cbName = exception.getCausingCircuitBreakerName();
        var message = "register".equals(cbName) ? "Registration is currently disabled." : exception.getMessage();
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse(message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CommonResponse> handleServiceExceptions(Exception exception) {
        log.error("An unknown exception occurred.", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new CommonResponse(exception.getMessage()));
    }


}
