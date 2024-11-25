package com.emlakjet.ecommerce.service;

import com.emlakjet.ecommerce.dto.ProductDto;
import com.emlakjet.ecommerce.dto.ProductUpdateDto;
import com.emlakjet.ecommerce.exception.ServiceException;
import com.emlakjet.ecommerce.mapper.ProductMapper;
import com.emlakjet.ecommerce.model.Product;
import com.emlakjet.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {ProductService.class})
class ProductServiceTest {

    @Autowired
    private ProductService service;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private ProductMapper productMapper;

    @Test
    void create_shouldCreateProduct() {
        var productDto = ProductDto.builder().code("P00001").build();
        var product = Product.builder().id("1").build();
        var savedProduct = Product.builder().id("1").build();

        when(productMapper.toEntity(productDto)).thenReturn(product);
        when(productRepository.save(product)).thenReturn(savedProduct);
        when(productMapper.toDto(savedProduct)).thenReturn(productDto);

        var result = service.create(productDto);

        assertNotNull(result);
        assertEquals(productDto, result);
    }

    @Test
    void create_shouldThrowError() {
        var productDto = ProductDto.builder().code("P00001").build();
        var product = Product.builder().id("1").code("P00001").build();

        when(productRepository.findByCode(any())).thenReturn(Optional.of(product));

        var exception = assertThrowsExactly(ServiceException.class, () -> service.create(productDto));

        assertNotNull(exception);
        verify(productRepository, never()).save(any());
    }

    @Test
    void list_shouldReturnPaginatedProducts() {
        var pageable = Pageable.unpaged();
        var product = Product.builder().id("1").build();
        var productDto = ProductDto.builder().id("1").build();
        var productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(pageable)).thenReturn(productPage);
        when(productMapper.toDto(product)).thenReturn(productDto);

        var result = service.list(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(productDto, result.getContent().getFirst());
    }

    @Test
    void getByCode_shouldReturnProductOptional() {
        var productCode = "P00001";
        var product = Product.builder().code(productCode).build();

        when(productRepository.findByCode(productCode)).thenReturn(Optional.of(product));

        var result = service.getByCode(productCode);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(product, result.get());
    }

    @Test
    void getById_shouldReturnProductDto() {
        var productId = "1";
        var product = Product.builder().id(productId).build();
        var productDto = ProductDto.builder().id(productId).build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.toDto(product)).thenReturn(productDto);

        var result = service.getById(productId);

        assertNotNull(result);
        assertEquals(productDto, result);
    }

    @Test
    void getById_shouldThrowError() {
        var productId = "1";

        var exception = assertThrowsExactly(ServiceException.class, () -> service.getById(productId));

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
        verify(productRepository).findById(productId);

    }

    @Test
    void update_shouldUpdateProduct() {
        var productId = "1";
        var product = Product.builder().id(productId).code("P00001").build();
        var updateDto = ProductUpdateDto.builder().code("P00002").build();
        var updatedProduct = Product.builder().id(productId).code("P00002").build();
        var updatedProductDto = ProductDto.builder().id(productId).code("P00002").build();

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(productMapper.update(product, updateDto)).thenReturn(updatedProduct);
        when(productRepository.save(updatedProduct)).thenReturn(updatedProduct);
        when(productMapper.toDto(updatedProduct)).thenReturn(updatedProductDto);

        var result = service.update(productId, updateDto);

        assertNotNull(result);
        assertEquals(updatedProductDto, result);
    }

    @Test
    void update_shouldThrowBadRequestError() {
        var productId = "1";
        var updateDto = ProductUpdateDto.builder().build();

        var exception = assertThrowsExactly(ServiceException.class, () -> service.update(productId, updateDto));

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        verify(productMapper, never()).update(any(), any());
        verify(productRepository, never()).save(any());
    }

    @Test
    void update_shouldThrowNotFoundError() {
        var productId = "1";
        var updateDto = ProductUpdateDto.builder().code("P00002").build();

        var exception = assertThrowsExactly(ServiceException.class, () -> service.update(productId, updateDto));

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void delete_shouldDeleteProduct() {
        var productId = "1";

        service.delete(productId);

        verify(productRepository).deleteById(productId);
    }
}