package com.emlakjet.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    @Schema(description = "JWT of the user", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huQGRvZS5jb20iLCJpYXQiOjE3MzI0NjMwNDcsImV4cCI6MTczMjQ2MzA0N30.mBWOXKlDkBykK5RrIZYQ5-5PxMIcytriPGQ2CUXiEyg")
    private String token;

    @Schema(description = "Expiration time of the token in milliseconds", example = "3600000")
    private long expiresIn;
}
