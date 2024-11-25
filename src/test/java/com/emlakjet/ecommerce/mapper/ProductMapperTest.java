package com.emlakjet.ecommerce.mapper;

import com.emlakjet.ecommerce.dto.ProductDto;
import com.emlakjet.ecommerce.dto.ProductUpdateDto;
import com.emlakjet.ecommerce.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {ProductMapper.class})
class ProductMapperTest {

    private final ProductMapper mapper = new ProductMapper();

    @Test
    void testToDto() {
        var product = Product.builder()
                .id("1")
                .code("P123")
                .name("Product Name")
                .description("Product Description")
                .price(99.99)
                .createdBy("User123")
                .build();

        var dto = mapper.toDto(product);

        assertNotNull(dto);
        assertEquals("1", dto.getId());
        assertEquals("P123", dto.getCode());
        assertEquals("Product Name", dto.getName());
        assertEquals("Product Description", dto.getDescription());
        assertEquals(99.99, dto.getPrice());
    }

    @Test
    void testToDto_shouldReturnNull() {
        var entity = mapper.toDto(null);
        assertNull(entity);
    }

    @Test
    void testToEntity() {
        var dto = ProductDto.builder()
                .id("1")
                .code("P123")
                .name("Product Name")
                .description("Product Description")
                .price(99.99)
                .build();

        var entity = mapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals("1", entity.getId());
        assertEquals("P123", entity.getCode());
        assertEquals("Product Name", entity.getName());
        assertEquals("Product Description", entity.getDescription());
        assertEquals(99.99, entity.getPrice());
    }

    @Test
    void testToEntity_shouldReturnNull() {
        var entity = mapper.toEntity(null);
        assertNull(entity);
    }

    @Test
    void testToDtos() {
        var product1 = Product.builder().id("1").code("P1").name("Name1").description("Desc1").price(10.0).build();
        var product2 = Product.builder().id("2").code("P2").name("Name2").description("Desc2").price(20.0).build();
        var products = List.of(product1, product2);

        var dtos = mapper.toDtos(products);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("1", dtos.get(0).getId());
        assertEquals("P2", dtos.get(1).getCode());
    }

    @Test
    void toDtos_shouldReturnEmptyList() {
        var result = mapper.toDtos(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testToEntities() {
        var dto1 = ProductDto.builder().id("1").code("P1").name("Name1").description("Desc1").price(10.0).build();
        var dto2 = ProductDto.builder().id("2").code("P2").name("Name2").description("Desc2").price(20.0).build();
        var dtos = List.of(dto1, dto2);

        var entities = mapper.toEntities(dtos);

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals("1", entities.get(0).getId());
        assertEquals("P2", entities.get(1).getCode());
    }

    @Test
    void toEntities_shouldReturnEmptyList() {
        var result = mapper.toEntities(null);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void update_shouldReturnEntity() {
        var entity = Product.builder().id("1").code("P001").name("Product").build();

        var result = mapper.update(entity, null);

        assertNotNull(result);
        assertEquals(entity, result);
    }

    @Test
    void update_shouldUpdate() {
        var entity = Product.builder().id("1").code("P001").build();
        var updateDto = ProductUpdateDto.builder().code("P002").build();

        var result = mapper.update(entity, updateDto);

        assertNotNull(result);
        assertEquals("P002", result.getCode());
    }

    @Test
    void update_shouldNotUpdate() {
        var entity = Product.builder().id("1").code("P001").build();
        var updateDto = ProductUpdateDto.builder().code("   ").build();

        var result = mapper.update(entity, updateDto);

        assertNotNull(result);
        assertEquals("P001", result.getCode());
    }

    @Test
    void update_shouldUpdateName() {
        var entity = Product.builder().id("1").name("Old Name").build();
        var updateDto = ProductUpdateDto.builder().name("New Name").build();

        var result = mapper.update(entity, updateDto);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
    }

    @Test
    void update_shouldNotUpdateName() {
        var entity = Product.builder().id("1").name("Old Name").build();
        var updateDto = ProductUpdateDto.builder().name("   ").build();

        var result = mapper.update(entity, updateDto);

        assertNotNull(result);
        assertEquals("Old Name", result.getName());
    }

    @Test
    void update_shouldUpdateDescription() {
        var entity = Product.builder().id("1").description("Old Description").build();
        var updateDto = ProductUpdateDto.builder().description("New Description").build();

        var result = mapper.update(entity, updateDto);

        assertNotNull(result);
        assertEquals("New Description", result.getDescription());
    }

    @Test
    void update_shouldUpdatePrice() {
        var entity = Product.builder().id("1").price(100.0).build();
        var updateDto = ProductUpdateDto.builder().price(150.0).build();

        var result = mapper.update(entity, updateDto);

        assertNotNull(result);
        assertEquals(150.0, result.getPrice());
    }

    @Test
    void update_shouldNotUpdatePrice() {
        var entity = Product.builder().id("1").price(100.0).build();
        var updateDto = ProductUpdateDto.builder().build();

        var result = mapper.update(entity, updateDto);

        assertNotNull(result);
        assertEquals(100.0, result.getPrice());
    }

}