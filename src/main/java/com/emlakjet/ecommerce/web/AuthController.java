package com.emlakjet.ecommerce.web;


import com.emlakjet.ecommerce.dto.AuthResponse;
import com.emlakjet.ecommerce.dto.LoginRequest;
import com.emlakjet.ecommerce.dto.RegisterRequest;
import com.emlakjet.ecommerce.service.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.emlakjet.ecommerce.constants.SwaggerExamples.EMAIL_ALREADY_EXISTS_RESPONSE;
import static com.emlakjet.ecommerce.constants.SwaggerExamples.INVALID_CREDENTIALS_RESPONSE;


@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/auth")
@Tag(name = "Authentication Controller", description = "API to authenticate and register users")
public class AuthController {

    private final AuthService authService;

    @ApiResponse(responseCode = "201", description = "Successfully registered user")
    @ApiResponse(responseCode = "409", description = "User with given email address already exists",
            content = @Content(examples = @ExampleObject(value = EMAIL_ALREADY_EXISTS_RESPONSE)))
    @PostMapping(value = "/v1/register", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterRequest registerRequest) {
        return authService.register(registerRequest);
    }

    @ApiResponse(responseCode = "200", description = "Successfully logged in")
    @ApiResponse(responseCode = "401", description = "Invalid credentials",
            content = @Content(examples = @ExampleObject(value = INVALID_CREDENTIALS_RESPONSE)))
    @PostMapping(value = "/v1/login", produces = MediaType.APPLICATION_JSON_VALUE)

    public AuthResponse login(@Valid @RequestBody LoginRequest loginRequest) {
        return authService.authenticate(loginRequest);
    }
}
