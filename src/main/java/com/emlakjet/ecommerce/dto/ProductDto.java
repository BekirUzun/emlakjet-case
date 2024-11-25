package com.emlakjet.ecommerce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProductDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY,
            description = "ID of the product",
            example = "6741da3c38bac77c991e3a17")
    private String id;

    @NotNull
    @Pattern(regexp = "\\S{6}")
    @Schema(description = "Six character long product code", example = "P00001")
    private String code;

    @NotNull
    @Size(min = 2, max = 100)
    @Schema(description = "Name of the product", example = "Product 1")
    private String name;

    @NotNull
    @Size(min = 2, max = 100)
    @Schema(description = "Short description of the product", example = "Very nice product")
    private String description;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("999999999999")
    @Schema(description = "Price of the product", example = "50")
    private Double price;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY,
            description = "ID of the user who created the product",
            example = "6740a9f2a4cdb00fd22b6117")
    private String createdBy;
}
