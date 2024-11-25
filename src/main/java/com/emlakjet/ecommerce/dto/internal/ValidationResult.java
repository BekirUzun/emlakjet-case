package com.emlakjet.ecommerce.dto.internal;

import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ValidationResult {
    private boolean isSuccessful;

    @Nullable
    private String message;

    public ValidationResult(boolean isSuccessful) {
        this.isSuccessful = isSuccessful;
    }
}
