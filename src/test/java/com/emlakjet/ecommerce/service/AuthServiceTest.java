package com.emlakjet.ecommerce.service;

import com.emlakjet.ecommerce.dto.LoginRequest;
import com.emlakjet.ecommerce.dto.RegisterRequest;
import com.emlakjet.ecommerce.exception.ServiceException;
import com.emlakjet.ecommerce.model.CommerceUser;
import com.emlakjet.ecommerce.repository.UserRepository;
import com.emlakjet.ecommerce.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {AuthService.class})
class AuthServiceTest {

    @Autowired
    private AuthService authService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private AuthUtil authUtil;


    @Test
    void register_shouldRegisterUser() {
        var registerRequest = RegisterRequest.builder()
                .email("test@example.com")
                .password("password")
                .fullName("John Doe")
                .build();
        var commerceUser = CommerceUser.builder().email("test@example.com").build();
        var authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(commerceUser);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(authUtil.generateToken(any())).thenReturn("token");
        when(authUtil.getExpirationTime()).thenReturn(3600L);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        var result = authService.register(registerRequest);

        assertNotNull(result);
        assertEquals("token", result.getToken());
        assertEquals(3600L, result.getExpiresIn());
    }

    @Test
    void register_shouldThrowServiceException() {
        var registerRequest = RegisterRequest.builder().email("test@example.com").build();
        var user = CommerceUser.builder().email("test@example.com").build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));

        var exception = assertThrows(ServiceException.class, () -> authService.register(registerRequest));

        assertNotNull(exception);
        assertEquals(HttpStatus.CONFLICT, exception.getHttpStatus());
    }

    @Test
    void authenticate_ShouldReturnAuthResponse_WhenCredentialsAreValid() {
        var loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();

        var commerceUser = CommerceUser.builder().email("test@example.com").build();
        var authentication = mock(Authentication.class);

        when(authentication.getPrincipal()).thenReturn(commerceUser);

        when(authUtil.generateToken(any())).thenReturn("token");
        when(authUtil.getExpirationTime()).thenReturn(3600L);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);

        var result = authService.authenticate(loginRequest);

        assertNotNull(result);
        assertEquals("token", result.getToken());
        assertEquals(3600L, result.getExpiresIn());
    }

    @Test
    void authenticate_shouldThrowServiceException() {
        var loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("wrongPassword")
                .build();

        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        var exception = assertThrows(ServiceException.class, () -> authService.authenticate(loginRequest));

        assertNotNull(exception);
        assertEquals(HttpStatus.UNAUTHORIZED, exception.getHttpStatus());
    }
}