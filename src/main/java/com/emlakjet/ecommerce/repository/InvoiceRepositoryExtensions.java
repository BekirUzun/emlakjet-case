package com.emlakjet.ecommerce.repository;

public interface InvoiceRepositoryExtensions {
    Double invoiceAmountTotal(String userId, Boolean isApproved);
}
