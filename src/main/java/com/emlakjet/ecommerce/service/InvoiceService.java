package com.emlakjet.ecommerce.service;

import com.emlakjet.ecommerce.dto.InvoiceCreationResult;
import com.emlakjet.ecommerce.dto.InvoiceDto;
import com.emlakjet.ecommerce.dto.internal.ValidationResult;
import com.emlakjet.ecommerce.exception.ServiceException;
import com.emlakjet.ecommerce.mapper.InvoiceMapper;
import com.emlakjet.ecommerce.mapper.ProductMapper;
import com.emlakjet.ecommerce.model.Invoice;
import com.emlakjet.ecommerce.repository.InvoiceRepository;
import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import static com.emlakjet.ecommerce.constants.Messages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoiceRepository;
    private final NotificationService notificationService;
    private final ProductService productService;
    private final ProductMapper productMapper;

    @Value("${ecommerce.invoiceLimit}")
    private Double invoiceLimitPerUser;

    /**
     * Creates a new invoice for the given user
     *
     * @param invoiceDto     the invoice to create
     * @param commerceUserId the user that the invoice belongs to
     * @return the result of the creation operation, including the created invoice
     * if the operation was successful
     */
    public InvoiceCreationResult create(InvoiceDto invoiceDto, String commerceUserId) {
        var invoice = InvoiceMapper.toEntity(invoiceDto);
        var validationResult = validateInvoice(invoice, commerceUserId);
        invoice.setIsApproved(validationResult.isSuccessful());
        var savedInvoice = invoiceRepository.save(invoice);

        if (Boolean.FALSE.equals(invoice.getIsApproved())) {
            var notificationMsg = String.format(
                    "%s %n> userId: `%s`, invoiceId: `%s`",
                    validationResult.getMessage(),
                    savedInvoice.getCreatedBy(),
                    savedInvoice.getId()
            );
            notificationService.sendNotification(notificationMsg);
        }
        return InvoiceCreationResult.builder()
                .isSuccessful(validationResult.isSuccessful())
                .message(validationResult.isSuccessful() ? INVOICE_APPROVED : validationResult.getMessage())
                .invoice(InvoiceMapper.toDto(savedInvoice))
                .build();
    }

    /**
     * Retrieves a paginated list of invoices for a specific user.
     *
     * @param commerceUserId the ID of the user whose invoices are to be retrieved
     * @param isApproved     optional filter to retrieve only approved or unapproved invoices
     * @param pageable       pagination information
     * @return a page containing the invoices mapped to InvoiceDto
     */
    public Page<InvoiceDto> getInvoices(String commerceUserId, @Nullable Boolean isApproved, Pageable pageable) {
        if (isApproved == null) {
            return invoiceRepository
                    .findByCreatedBy(commerceUserId, pageable)
                    .map(InvoiceMapper::toDto);
        }
        return invoiceRepository
                .findByCreatedByAndIsApproved(commerceUserId, isApproved, pageable)
                .map(InvoiceMapper::toDto);
    }

    /**
     * Retrieves an invoice by its ID.
     *
     * @param invoiceId the ID of the invoice to retrieve
     * @return the retrieved invoice
     * @throws ServiceException if the invoice does not exist
     */
    public InvoiceDto getInvoice(String invoiceId) {
        var invoiceOpt = invoiceRepository.findById(invoiceId);
        if (invoiceOpt.isEmpty()) {
            throw new ServiceException(HttpStatus.NOT_FOUND, INVOICE_NOT_FOUND);
        }
        var invoiceDto = InvoiceMapper.toDto(invoiceOpt.get());
        var productOpt = productService.getByCode(invoiceOpt.get().getProductCode());
        productOpt.ifPresent(product -> invoiceDto.setProduct(productMapper.toDto(product)));
        return invoiceDto;
    }

    /**
     * Validates an invoice by checking if the total amount of all approved invoices
     * including the given invoice exceeds the limit per user.
     *
     * @param invoice the invoice to be validated
     * @param userId  the ID of the user who submitted the invoice
     * @return a ValidationResult containing a boolean indicating success or failure
     * and a message describing the reason for failure.
     */
    public ValidationResult validateInvoice(Invoice invoice, String userId) {
        var existingInvoices = invoiceRepository.findAllByBillNoAndIsApproved(invoice.getBillNo(), true);
        if (!CollectionUtils.isEmpty(existingInvoices)) {
            return new ValidationResult(false, INVOICE_BILL_NO_EXISTS);
        }

        var approvedTotalAmount = invoiceRepository.invoiceAmountTotal(userId, true);
        if (approvedTotalAmount + invoice.getAmount() > invoiceLimitPerUser) {

            var responseMsg = String.format(INVOICE_LIMIT_EXCEEDED,
                    approvedTotalAmount,
                    invoice.getAmount(),
                    invoiceLimitPerUser
            );
            return new ValidationResult(false, responseMsg);
        }

        return new ValidationResult(true);
    }

    public void deleteInvoice(String invoiceId) {
        invoiceRepository.deleteById(invoiceId);
    }
}
