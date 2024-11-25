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
public class RegisterRequest {

    @NotNull
    @Size(min = 6, max = 320)
    @Schema(example = "john@doe.com")
    private String email;

    @NotNull
    @Size(min = 5, max = 255)
    @Schema(example = "John Doe")
    private String fullName;

    @NotNull
    @Size(min = 6, max = 255)
    @Schema(example = "john123")
    private String password;
}
