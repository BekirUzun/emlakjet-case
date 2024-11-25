package com.emlakjet.ecommerce.web;

import com.emlakjet.ecommerce.dto.InvoiceCreationResult;
import com.emlakjet.ecommerce.dto.InvoiceDto;
import com.emlakjet.ecommerce.service.InvoiceService;
import com.emlakjet.ecommerce.util.AuthUtil;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class InvoiceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InvoiceService invoiceService;

    @MockBean
    private AuthUtil authUtil;

    @Test
    void createInvoice_approved() throws Exception {
        var payloadCaptor = ArgumentCaptor.forClass(InvoiceDto.class);
        var invoiceCreationResult = InvoiceCreationResult.builder().isSuccessful(true).build();
        when(authUtil.getUserId()).thenReturn("userId");
        when(invoiceService.create(any(), any())).thenReturn(invoiceCreationResult);

        var resultActions = mockMvc.perform(post("/invoice/v1")
                .contentType("application/json")
                .content("""
                        {
                           "firstName": "John",
                           "lastName": "Doe",
                           "email": "john@doe.com",
                           "amount": 100,
                           "productCode": "P00001",
                           "billNo": "FYS2024000000001"
                         }"""));

        resultActions.andExpect(status().isCreated());

        verify(invoiceService).create(payloadCaptor.capture(), any());
        var payload = payloadCaptor.getValue();
        assertEquals("John", payload.getFirstName());
        assertEquals("Doe", payload.getLastName());
        assertEquals("john@doe.com", payload.getEmail());
        assertEquals(100, payload.getAmount());
        assertEquals("FYS2024000000001", payload.getBillNo());
    }

    @Test
    void createInvoice_notApproved() throws Exception {
        var payloadCaptor = ArgumentCaptor.forClass(InvoiceDto.class);
        var invoiceCreationResult = InvoiceCreationResult.builder().isSuccessful(false).build();
        when(authUtil.getUserId()).thenReturn("userId");
        when(invoiceService.create(any(), any())).thenReturn(invoiceCreationResult);

        var resultActions = mockMvc.perform(post("/invoice/v1")
                .contentType("application/json")
                .content("""
                        {
                           "firstName": "John",
                           "lastName": "Doe",
                           "email": "john@doe.com",
                           "amount": 100,
                           "productCode": "P00001",
                           "billNo": "FYS2024000000001"
                         }"""));

        resultActions.andExpect(status().isUnprocessableEntity());

        verify(invoiceService).create(payloadCaptor.capture(), any());
        var payload = payloadCaptor.getValue();
        assertEquals("John", payload.getFirstName());

    }

    @Test
    void createInvoice_badRequest() throws Exception {
        var invoiceCreationResult = InvoiceCreationResult.builder().isSuccessful(false).build();
        when(authUtil.getUserId()).thenReturn("userId");
        when(invoiceService.create(any(), any())).thenReturn(invoiceCreationResult);

        var resultActions = mockMvc.perform(post("/invoice/v1")
                .contentType("application/json")
                .content("{ \"firstName\": \"John\" }"));

        resultActions.andExpect(status().isBadRequest());

        verify(invoiceService, never()).create(any(), any());
    }


    @Test
    void listInvoices() throws Exception {
        when(authUtil.getUserId()).thenReturn("userId");
        var invoiceDto = InvoiceDto.builder().amount(100.0).build();
        var pageable = Pageable.ofSize(10).withPage(0);
        var invoicePage = new PageImpl<>(List.of(invoiceDto), pageable, 1);
        when(invoiceService.getInvoices(any(), any(), any())).thenReturn(invoicePage);

        var resultActions = mockMvc.perform(get("/invoice/v1"));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(containsString("amount")));
        verify(invoiceService).getInvoices(any(), any(), any());

    }

    @Test
    void getInvoice() throws Exception {
        var invoiceId = "123";
        var invoiceDto = InvoiceDto.builder().id(invoiceId).build();
        when(invoiceService.getInvoice(invoiceId)).thenReturn(invoiceDto);

        var resultActions = mockMvc.perform(get("/invoice/v1/" + invoiceId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultActions.andExpect(status().isOk());
        resultActions.andExpect(content().string(containsString(String.valueOf(invoiceDto.getId()))));
        verify(invoiceService).getInvoice(invoiceId);
    }

    @Test
    void deleteInvoice() throws Exception {
        var invoiceId = "123";

        var resultActions = mockMvc.perform(delete("/invoice/v1/" + invoiceId)
                .contentType(MediaType.APPLICATION_JSON_VALUE));

        resultActions.andExpect(status().isNoContent());
        verify(invoiceService).deleteInvoice(invoiceId);
    }

}