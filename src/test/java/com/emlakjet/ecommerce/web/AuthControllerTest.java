package com.emlakjet.ecommerce.web;

import com.emlakjet.ecommerce.dto.AuthResponse;
import com.emlakjet.ecommerce.dto.LoginRequest;
import com.emlakjet.ecommerce.dto.RegisterRequest;
import com.emlakjet.ecommerce.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void register() throws Exception {
        var registerRequest = RegisterRequest.builder()
                .email("john@doe.com")
                .fullName("John Doe")
                .password("password")
                .build();
        var authResponse = AuthResponse.builder().token("jwt-token").build();
        when(authService.register(any())).thenReturn(authResponse);

        var resultActions = mockMvc.perform(post("/auth/v1/register")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(registerRequest)));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(content().string(containsString(authResponse.getToken())));
        verify(authService).register(any());
    }

    @Test
    void login() throws Exception {
        var loginRequest = LoginRequest.builder()
                .email("test@example.com")
                .password("password")
                .build();
        var authResponse = AuthResponse.builder().token("jwt-token").build();
        when(authService.authenticate(any())).thenReturn(authResponse);

        var resultActions = mockMvc.perform(post("/auth/v1/login")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(loginRequest)));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(containsString(authResponse.getToken())));
        verify(authService).authenticate(any());
    }

}