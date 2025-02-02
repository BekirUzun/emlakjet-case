package com.emlakjet.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class CommonResponse {

    @Schema(description = "Message explaining the situation", example = "User with given email address already exists")
    private String message;
}
