package com.emlakjet.ecommerce.config;

import com.emlakjet.ecommerce.exception.ServiceException;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {GlobalExceptionHandler.class})
class GlobalExceptionHandlerTest {

    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleValidationExceptions() {
        var exception = mock(MethodArgumentNotValidException.class);
        var bindingResult = mock(BindingResult.class);
        var fieldError = new FieldError("objectName", "field", "defaultMessage");
        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        var result = globalExceptionHandler.handleValidationExceptions(exception);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void handleServiceExceptions() {
        var exception = new ServiceException(HttpStatus.NOT_FOUND, "message");

        var result = globalExceptionHandler.handleServiceExceptions(exception);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody());
    }

    @Test
    void handleServiceExceptions_HttpMessageNotReadableException() {
        var exception = new HttpMessageNotReadableException("message", mock(HttpInputMessage.class));

        var result = globalExceptionHandler.handleServiceExceptions(exception);

        assertNotNull(result);
        assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("message", result.getBody().getMessage());
    }

    @Test
    void handleServiceExceptions_ExpiredJwtException() {
        var exception = new ExpiredJwtException(null, null, "message");

        var result = globalExceptionHandler.handleServiceExceptions(exception);

        assertNotNull(result);
        assertEquals(HttpStatus.UNAUTHORIZED, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("message", result.getBody().getMessage());
    }

    @Test
    void handleServiceExceptions_ServiceException() {
        var exception = new ServiceException(HttpStatus.NOT_FOUND, "message");

        var result = globalExceptionHandler.handleServiceExceptions(exception);

        assertNotNull(result);
        assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("message", result.getBody().getMessage());
    }

    @Test
    void handleCircuitBreakerExceptions() {
        var exception = mock(CallNotPermittedException.class);

        when(exception.getCausingCircuitBreakerName()).thenReturn("register");
        when(exception.getMessage()).thenReturn("Default message");

        var result = globalExceptionHandler.handleCircuitBreakerExceptions(exception);

        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("Registration is currently disabled.", result.getBody().getMessage());
    }

    @Test
    void handleServiceExceptions_GenericException() {
        var exception = new Exception("message");

        var result = globalExceptionHandler.handleServiceExceptions(exception);

        assertNotNull(result);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals("message", result.getBody().getMessage());
    }
}