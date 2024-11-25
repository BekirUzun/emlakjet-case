package com.emlakjet.ecommerce.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Messages {
    public static final String INVOICE_APPROVED = "Invoice approved and saved successfully.";

    public static final String NOTIFICATION_FAILED = "Failed to send notification";
    public static final String INVOICE_LIMIT_EXCEEDED = "Invoice is not approved. Total invoice amount is above the limit. " +
            "Already Approved: %s, Invoice: %s, Limit: %s";
    public static final String INVOICE_BILL_NO_EXISTS = "Given bill no already exists.";
    public static final String INVOICE_NOT_FOUND = "Invoice with given ID doesn't exists.";

    public static final String INVALID_TOKEN = "Authorization token is not provided or invalid";
    public static final String EXPIRED_TOKEN = "Authorization token is expired";
    public static final String EMAIL_ALREADY_EXISTS = "User with same E-mail already exists";
    public static final String INVALID_CREDENTIALS = "Invalid email or password";

    public static final String PRODUCT_NOT_FOUND = "Product with given ID doesn't exists.";
    public static final String NO_PROPERTIES_TO_UPDATE = "No properties given to update.";
    public static final String PRODUCT_WITH_SAME_CODE_EXISTS = "Another product with same code already exists.";
}
