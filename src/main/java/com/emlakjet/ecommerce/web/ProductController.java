package com.emlakjet.ecommerce.web;


import com.emlakjet.ecommerce.dto.CommonResponse;
import com.emlakjet.ecommerce.dto.PagedResponse;
import com.emlakjet.ecommerce.dto.ProductDto;
import com.emlakjet.ecommerce.dto.ProductUpdateDto;
import com.emlakjet.ecommerce.service.ProductService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.emlakjet.ecommerce.constants.SwaggerExamples.*;


@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/product")
@Tag(name = "Product Controller", description = "Product operations")
public class ProductController {

    private final ProductService productService;

    @ApiResponse(responseCode = "201", description = "Successfully created the resource")
    @ApiResponse(responseCode = "400", description = "Invalid request",
            content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    @ApiResponse(responseCode = "409", description = "Product with given code already exists",
            content = @Content(examples = @ExampleObject(value = PRODUCT_WITH_SAME_CODE_RESPONSE)))
    @PostMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto create(@Valid @RequestBody ProductDto productDto) {
        return productService.create(productDto);
    }


    @ApiResponse(responseCode = "200", description = "List of products")
    @ApiResponse(responseCode = "401", description = "Authentication problem",
            content = @Content(examples = @ExampleObject(value = INVALID_TOKEN_RESPONSE)))
    @GetMapping(value = "/v1", produces = MediaType.APPLICATION_JSON_VALUE)
    public PagedResponse<ProductDto> listProducts(
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize
    ) {
        var productPage = productService.list(Pageable.ofSize(pageSize).withPage(page));
        return new PagedResponse<>(productPage);
    }

    @ApiResponse(responseCode = "200", description = "Product details")
    @ApiResponse(responseCode = "401", description = "Authentication problem",
            content = @Content(examples = @ExampleObject(value = INVALID_TOKEN_RESPONSE)))
    @ApiResponse(responseCode = "404", description = "Product not found",
            content = @Content(examples = @ExampleObject(value = PRODUCT_NOT_FOUND_RESPONSE)))
    @GetMapping(value = "/v1/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto getProduct(@PathVariable(value = "productId") String productId) {
        return productService.getById(productId);
    }

    @ApiResponse(responseCode = "200", description = "Product updated successfully")
    @ApiResponse(responseCode = "400", description = "Invalid request",
            content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Authentication problem",
            content = @Content(examples = @ExampleObject(value = INVALID_TOKEN_RESPONSE)))
    @PutMapping(value = "/v1/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ProductDto updateProduct(
            @PathVariable(value = "productId") String productId,
            @Valid @RequestBody ProductUpdateDto productDto
    ) {
        return productService.update(productId, productDto);
    }

    @ApiResponse(responseCode = "204", description = "Product deleted successfully")
    @ApiResponse(responseCode = "404", description = "Given productId does not exist",
            content = @Content(schema = @Schema(implementation = CommonResponse.class)))
    @ApiResponse(responseCode = "401", description = "Authentication problem",
            content = @Content(examples = @ExampleObject(value = INVALID_TOKEN_RESPONSE)))
    @DeleteMapping(value = "/v1/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable(value = "productId") String productId) {
        productService.delete(productId);
    }


}
