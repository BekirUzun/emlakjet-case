package com.emlakjet.ecommerce.mapper;

import com.emlakjet.ecommerce.dto.InvoiceDto;
import com.emlakjet.ecommerce.model.Invoice;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith({SpringExtension.class})
class InvoiceMapperTest {

    @Test
    void testToDto() {
        var invoice = Invoice.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .amount(100.0)
                .productCode("Product A")
                .billNo("12345")
                .createdBy("Admin")
                .isApproved(true)
                .build();

        var dto = InvoiceMapper.toDto(invoice);

        assertNotNull(dto);
        assertEquals(invoice.getId(), dto.getId());
        assertEquals(invoice.getFirstName(), dto.getFirstName());
        assertEquals(invoice.getLastName(), dto.getLastName());
        assertEquals(invoice.getEmail(), dto.getEmail());
        assertEquals(invoice.getAmount(), dto.getAmount());
        assertEquals(invoice.getProductCode(), dto.getProductCode());
        assertEquals(invoice.getBillNo(), dto.getBillNo());
        assertEquals(invoice.getCreatedBy(), dto.getCreatedBy());
        assertEquals(invoice.getIsApproved(), dto.getIsApproved());
    }

    @Test
    void testToDto_shouldReturnNull() {
        var dto = InvoiceMapper.toDto(null);

        assertNull(dto);
    }

    @Test
    void testToEntity() {
        var dto = InvoiceDto.builder()
                .id("1")
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .amount(100.0)
                .productCode("Product A")
                .billNo("12345")
                .createdBy("Admin")
                .build();

        var entity = InvoiceMapper.toEntity(dto);

        assertNotNull(entity);
        assertEquals(dto.getId(), entity.getId());
        assertEquals(dto.getFirstName(), entity.getFirstName());
        assertEquals(dto.getLastName(), entity.getLastName());
        assertEquals(dto.getEmail(), entity.getEmail());
        assertEquals(dto.getAmount(), entity.getAmount());
        assertEquals(dto.getProductCode(), entity.getProductCode());
        assertEquals(dto.getBillNo(), entity.getBillNo());
        assertNull(entity.getCreatedBy());
        assertNull(entity.getIsApproved());
    }

    @Test
    void testToEntity_shouldReturnNull() {
        var dto = InvoiceMapper.toEntity(null);

        assertNull(dto);
    }

    @Test
    void testToDtos() {
        var invoices = List.of(
                Invoice.builder().id("1").firstName("John").build(),
                Invoice.builder().id("2").firstName("Jane").build()
        );

        var dtos = InvoiceMapper.toDtos(invoices);

        assertNotNull(dtos);
        assertEquals(2, dtos.size());
        assertEquals("John", dtos.get(0).getFirstName());
        assertEquals("Jane", dtos.get(1).getFirstName());
    }

    @Test
    void testToDtos_shouldReturnEmptyList() {
        var dtos = InvoiceMapper.toDtos(null);

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

    @Test
    void testToEntities() {
        var dtos = List.of(
                InvoiceDto.builder().id("1").firstName("John").build(),
                InvoiceDto.builder().id("2").firstName("Jane").build()
        );

        var entities = InvoiceMapper.toEntities(dtos);

        assertNotNull(entities);
        assertEquals(2, entities.size());
        assertEquals("John", entities.get(0).getFirstName());
        assertEquals("Jane", entities.get(1).getFirstName());
    }

    @Test
    void testToEntities_shouldReturnEmptyList() {
        var dtos = InvoiceMapper.toEntities(null);

        assertNotNull(dtos);
        assertTrue(dtos.isEmpty());
    }

}