package com.emlakjet.ecommerce.service;


import com.emlakjet.ecommerce.dto.InvoiceDto;
import com.emlakjet.ecommerce.exception.ServiceException;
import com.emlakjet.ecommerce.mapper.InvoiceMapper;
import com.emlakjet.ecommerce.mapper.ProductMapper;
import com.emlakjet.ecommerce.model.Invoice;
import com.emlakjet.ecommerce.repository.InvoiceRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest(classes = {InvoiceService.class})
class InvoiceServiceTest {

    @MockBean
    private InvoiceRepository invoiceRepository;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @Autowired
    private InvoiceService invoiceService;


    @Test
    void create_shouldReturnSuccessful() {
        var invoiceDto = InvoiceDto.builder().amount(100.0).build();
        var invoice = InvoiceMapper.toEntity(invoiceDto);
        when(invoiceRepository.save(any())).thenReturn(invoice);

        var result = invoiceService.create(invoiceDto, "user-123");

        assertNotNull(result);
        assertTrue(result.isSuccessful());
        assertNotNull(result.getInvoice());
    }

    @Test
    void create_shouldReturnFailedBillNo() {
        var invoiceDto = InvoiceDto.builder().amount(100.0).build();
        var existingInvoice = Invoice.builder().isApproved(true).build();
        var savedInvoice = Invoice.builder().isApproved(false).build();

        when(invoiceRepository.save(any())).thenReturn(savedInvoice);
        when(invoiceRepository.findAllByBillNoAndIsApproved(any(), any())).thenReturn(List.of(existingInvoice));

        var result = invoiceService.create(invoiceDto, "userId");

        assertNotNull(result);
        assertFalse(result.isSuccessful());
        verify(notificationService).sendNotification(any());
    }

    @Test
    void create_shouldReturnFailedLimit() {
        var invoiceDto = InvoiceDto.builder().amount(100D).build();
        var savedInvoice = Invoice.builder().isApproved(false).build();

        when(invoiceRepository.save(any())).thenReturn(savedInvoice);
        when(invoiceRepository.invoiceAmountTotal(any(), any())).thenReturn(180D);

        var result = invoiceService.create(invoiceDto, "userId");

        assertNotNull(result);
        assertFalse(result.isSuccessful());
        verify(notificationService).sendNotification(any());
        verify(invoiceRepository).invoiceAmountTotal(any(), any());
    }

    @Test
    void getInvoices_success() {
        var invoice = Invoice.builder().build();
        var pageable = Pageable.unpaged();
        Page<Invoice> page = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findByCreatedBy("userId", pageable)).thenReturn(page);

        var result = invoiceService.getInvoices("userId", null, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getInvoices_shouldReturnApprovedInvoices() {
        var invoice = Invoice.builder().build();
        var pageable = Pageable.unpaged();
        Page<Invoice> page = new PageImpl<>(List.of(invoice));

        when(invoiceRepository.findByCreatedByAndIsApproved("userId", true, pageable))
                .thenReturn(page);

        var result = invoiceService.getInvoices("userId", true, pageable);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());
    }

    @Test
    void getInvoice_success() {
        var invoice = Invoice.builder().productCode("P001").build();

        when(invoiceRepository.findById("invoiceId")).thenReturn(Optional.of(invoice));
        when(productService.getByCode("P001")).thenReturn(Optional.empty());

        var result = invoiceService.getInvoice("invoiceId");

        assertNotNull(result);
    }

    @Test
    void getInvoice_notFound() {
        var exception = assertThrowsExactly(ServiceException.class, () ->
                invoiceService.getInvoice("invoiceId"));

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getHttpStatus());
    }

    @Test
    void deleteInvoice_success() {
        invoiceService.deleteInvoice("invoiceId");

        verify(invoiceRepository, times(1)).deleteById("invoiceId");
    }

}