package com.emlakjet.ecommerce.web;

import com.emlakjet.ecommerce.dto.ProductDto;
import com.emlakjet.ecommerce.dto.ProductUpdateDto;
import com.emlakjet.ecommerce.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void createProduct() throws Exception {
        var productDto = ProductDto.builder()
                .id("123")
                .code("P00001")
                .price(10.0)
                .description("Product Description")
                .name("Product 1")
                .build();
        when(productService.create(any())).thenReturn(productDto);

        var resultActions = mockMvc.perform(post("/product/v1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(productDto)));

        resultActions.andExpect(status().isCreated());
        resultActions.andExpect(content().string(containsString(productDto.getId())));
        verify(productService).create(any());
    }

    @Test
    void createProduct_invalidProductCode() throws Exception {
        var productDto = ProductDto.builder()
                .id("123")
                .code("invalid code")
                .price(10.0)
                .description("Product Description")
                .name("Product 1")
                .build();
        when(productService.create(any())).thenReturn(productDto);

        var resultActions = mockMvc.perform(post("/product/v1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(productDto)));

        resultActions.andExpect(status().isBadRequest());
        resultActions.andExpect(content().string(containsString("FieldError")));
        verify(productService, never()).create(any());
    }

    @Test
    void listProducts() throws Exception {
        var productDto = ProductDto.builder().id("123").name("Product 1").build();
        var pageable = Pageable.ofSize(10).withPage(0);
        var productPage = new PageImpl<>(List.of(productDto), pageable, 1);
        when(productService.list(any())).thenReturn(productPage);

        var resultActions = mockMvc.perform(get("/product/v1")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .param("page", "0")
                .param("pageSize", "10"));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(containsString(productDto.getName())));
        verify(productService).list(any());
    }

    @Test
    void getProduct() throws Exception {
        var productId = "123";
        var productDto = ProductDto.builder().id(productId).name("Product 1").build();
        when(productService.getById(productId)).thenReturn(productDto);

        var resultActions = mockMvc.perform(get("/product/v1/" + productId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(containsString(productDto.getName())));
        verify(productService).getById(productId);
    }

    @Test
    void updateProduct() throws Exception {
        var productId = "123";
        var updateDto = ProductUpdateDto.builder().name("Updated Product").build();
        var updatedProductDto = ProductDto.builder().id(productId).name("Updated Product").build();
        when(productService.update(any(), any())).thenReturn(updatedProductDto);

        var resultActions = mockMvc.perform(put("/product/v1/" + productId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(updateDto)));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(containsString(updatedProductDto.getName())));
        verify(productService).update(eq(productId), any());
    }

    @Test
    void deleteProduct() throws Exception {
        var productId = "123";

        var resultActions = mockMvc.perform(delete("/product/v1/" + productId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultActions.andExpect(status().isNoContent());
        verify(productService).delete(productId);
    }

}