package com.emlakjet.ecommerce.service;

import com.emlakjet.ecommerce.dto.AuthResponse;
import com.emlakjet.ecommerce.dto.LoginRequest;
import com.emlakjet.ecommerce.dto.RegisterRequest;
import com.emlakjet.ecommerce.exception.ServiceException;
import com.emlakjet.ecommerce.model.CommerceUser;
import com.emlakjet.ecommerce.repository.UserRepository;
import com.emlakjet.ecommerce.util.AuthUtil;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import static com.emlakjet.ecommerce.constants.Messages.EMAIL_ALREADY_EXISTS;
import static com.emlakjet.ecommerce.constants.Messages.INVALID_CREDENTIALS;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;


    /**
     * Registers a new user with the provided registration details.
     *
     * @param registerRequest the registration request containing user details
     * @return an AuthResponse containing authentication token and expiration time
     * @throws ServiceException if a user with the provided email already exists
     */
    @CircuitBreaker(name = "register")
    public AuthResponse register(RegisterRequest registerRequest) {
        var existingUser = userRepository.findByEmail(registerRequest.getEmail());
        if (existingUser.isPresent()) {
            throw new ServiceException(HttpStatus.CONFLICT, EMAIL_ALREADY_EXISTS);
        }

        var user = CommerceUser.builder()
                .email(registerRequest.getEmail())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .fullName(registerRequest.getFullName())
                .build();

        userRepository.save(user);
        return authenticate(registerRequest.getEmail(), registerRequest.getPassword());
    }

    /**
     * Authenticates the user using the provided email and password.
     *
     * @param email    the user's email
     * @param password the user's password
     * @return an AuthResponse containing authentication token and expiration time
     * @throws ServiceException if the authentication fails
     */
    private AuthResponse authenticate(String email, String password) {
        try {
            var auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            var commerceUser = (CommerceUser) auth.getPrincipal();
            return AuthResponse.builder()
                    .token(authUtil.generateToken(commerceUser))
                    .expiresIn(authUtil.getExpirationTime())
                    .build();
        } catch (BadCredentialsException exception) {
            throw new ServiceException(HttpStatus.UNAUTHORIZED, INVALID_CREDENTIALS);
        }
    }

    /**
     * Authenticates the user using the provided login details.
     *
     * @param loginRequest the login request containing user email and password
     * @return an AuthResponse containing authentication token and expiration time
     * @throws ServiceException if the authentication fails
     */
    public AuthResponse authenticate(LoginRequest loginRequest) {
        return authenticate(loginRequest.getEmail(), loginRequest.getPassword());
    }

}
