package com.emlakjet.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceDto {

    @Schema(accessMode = Schema.AccessMode.READ_ONLY,
            description = "ID of the invoice",
            example = "6741da3c38bac77c991e3a17")
    private String id;

    @NotNull
    @Size(min = 2, max = 100)
    @Schema(description = "First name of the invoice owner", example = "John")
    private String firstName;

    @NotNull
    @Size(min = 2, max = 100)
    @Schema(description = "Last name of the invoice owner", example = "Doe")
    private String lastName;

    @NotNull
    @Size(min = 6, max = 320)
    @Schema(description = "Email of the invoice owner", example = "john@doe.com")
    private String email;

    @NotNull
    @DecimalMin("0.01")
    @DecimalMax("999999999999")
    @Schema(description = "Amount of the invoice", example = "100")
    private Double amount;

    @NotNull
    @Pattern(regexp = "\\S{6}")
    @Schema(description = "Product code of the bought item", example = "P00001")
    private String productCode;

    @NotNull
    @Size(min = 1, max = 255)
    @Pattern(regexp = "\\w{3}\\d{13}")
    @Schema(description = "Bill no of the invoice", example = "FYS2024000000001")
    private String billNo;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY,
            description = "Creator of the invoice",
            example = "6740a9f2a4cdb00fd22b6117")
    private String createdBy;

    @Schema(accessMode = Schema.AccessMode.READ_ONLY, description = "Is invoice approved", example = "true")
    private Boolean isApproved;

    @Hidden
    private ProductDto product;
}
