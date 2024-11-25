package com.emlakjet.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotNull
    @Size(min = 6, max = 320)
    @Schema(description = "Email of the commerce user", example = "john@doe.com")
    private String email;

    @NotNull
    @Size(min = 6, max = 255)
    @Schema(description = "Password of the bought item", example = "john123")
    private String password;
}
