package com.emlakjet.ecommerce.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InvoiceCreationResult {
    private boolean isSuccessful;
    private String message;
    private InvoiceDto invoice;
}
