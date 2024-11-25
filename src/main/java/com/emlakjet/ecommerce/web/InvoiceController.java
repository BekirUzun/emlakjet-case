package com.emlakjet.ecommerce.web;


import com.emlakjet.ecommerce.dto.CommonResponse;
import com.emlakjet.ecommerce.dto.InvoiceCreationResult;
import com.emlakjet.ecommerce.dto.InvoiceDto;
import com.emlakjet.ecommerce.dto.PagedResponse;
import com.emlakjet.ecommerce.service.InvoiceService;
import com.emlakjet.ecommerce.util.AuthUtil;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.emlakjet.ecommerce.constants.SwaggerExamples.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/invoice")
@Tag(name = "Invoice Controller", description = "Invoice operations")
public class InvoiceController {

    private final InvoiceService invoiceService;
    private final AuthUtil authUtil;

    @ApiResponse(responseCode = "201", description = "Resource created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request",
            content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    @ApiResponse(responseCode = "422", description = "Unprocessable entity given",
            content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Authentication problem",
            content = @Content(examples = @ExampleObject(value = INVALID_TOKEN_RESPONSE)))
    @PostMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceCreationResult> createInvoice(
            @Valid @RequestBody @Parameter(content = @Content(examples = @ExampleObject(value = INVOICE_REQUEST)))
            InvoiceDto invoiceDto
    ) {
        var invoiceCreationResult = invoiceService.create(invoiceDto, authUtil.getUserId());

        return ResponseEntity
                .status(invoiceCreationResult.isSuccessful() ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY)
                .body(invoiceCreationResult);
    }


    @ApiResponse(responseCode = "200", description = "List of invoices with given parameters")
    @ApiResponse(responseCode = "401", description = "Authentication problem",
            content = @Content(examples = @ExampleObject(value = INVALID_TOKEN_RESPONSE)))
    @GetMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedResponse<InvoiceDto> listInvoices(
            @RequestParam(value = "isApproved", required = false) Boolean isApproved,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) {
        var invoicePage = invoiceService
                .getInvoices(authUtil.getUserId(), isApproved, Pageable.ofSize(pageSize).withPage(page));
        return new PagedResponse<>(invoicePage);
    }

    @ApiResponse(responseCode = "200", description = "Invoice details")
    @ApiResponse(responseCode = "401", description = "Authentication problem",
            content = @Content(examples = @ExampleObject(value = INVALID_TOKEN_RESPONSE)))
    @ApiResponse(responseCode = "404", description = "Invoice not found",
            content = @Content(examples = @ExampleObject(value = INVOICE_NOT_FOUND_RESPONSE)))
    @GetMapping(value = "/v1/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public InvoiceDto getInvoice(@PathVariable(value = "invoiceId") String invoiceId) {
        return invoiceService.getInvoice(invoiceId);
    }

    @DeleteMapping(value = "/v1/{invoiceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteInvoice(@PathVariable(value = "invoiceId") String invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
    }

}
