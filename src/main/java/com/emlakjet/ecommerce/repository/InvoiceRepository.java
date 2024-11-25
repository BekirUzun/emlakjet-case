package com.emlakjet.ecommerce.repository;

import com.emlakjet.ecommerce.model.Invoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InvoiceRepository extends CrudRepository<Invoice, String>, InvoiceRepositoryExtensions {

    List<Invoice> findAllByBillNoAndIsApproved(String billNo, Boolean isApproved);

    Page<Invoice> findByCreatedBy(String userId, Pageable pageable);

    Page<Invoice> findByCreatedByAndIsApproved(String userId, Boolean isApproved, Pageable pageable);

}
