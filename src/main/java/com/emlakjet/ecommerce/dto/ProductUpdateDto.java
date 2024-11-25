package com.emlakjet.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductUpdateDto {

    @Size(min = 6, max = 6)
    @Schema(example = "P00001")
    private String code;

    @Size(min = 2, max = 100)
    @Schema(example = "Product 1")
    private String name;

    @Size(min = 2, max = 100)
    @Schema(example = "Very nice product")
    private String description;

    @DecimalMin("0.01")
    @DecimalMax("999999999999")
    @Schema(example = "50")
    private Double price;

    public boolean hasPropertiesToUpdate() {
        return code != null || name != null || description != null || price != null;
    }
}
