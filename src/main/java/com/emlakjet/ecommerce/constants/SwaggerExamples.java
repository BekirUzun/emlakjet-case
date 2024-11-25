package com.emlakjet.ecommerce.constants;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import static com.emlakjet.ecommerce.constants.Messages.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SwaggerExamples {

    private static final String RESP_PREFIX = "{\"message\": \"";
    private static final String RESP_POSTFIX = "\" }";

    public static final String EMAIL_ALREADY_EXISTS_RESPONSE = RESP_PREFIX + EMAIL_ALREADY_EXISTS + RESP_POSTFIX;
    public static final String INVALID_CREDENTIALS_RESPONSE = RESP_PREFIX + INVALID_CREDENTIALS + RESP_POSTFIX;
    public static final String PRODUCT_WITH_SAME_CODE_RESPONSE = RESP_PREFIX + PRODUCT_WITH_SAME_CODE_EXISTS + RESP_POSTFIX;
    public static final String INVOICE_NOT_FOUND_RESPONSE = RESP_PREFIX + INVOICE_NOT_FOUND + RESP_POSTFIX;
    public static final String INVALID_TOKEN_RESPONSE = RESP_PREFIX + INVALID_TOKEN + RESP_POSTFIX;
    public static final String PRODUCT_NOT_FOUND_RESPONSE = RESP_PREFIX + PRODUCT_NOT_FOUND + RESP_POSTFIX;


    public static final String INVOICE_REQUEST = """
            {
              "firstName": "John",
              "lastName": "Doe",
              "email": "john@doe.com",
              "amount": 100,
              "productCode": "P00001",
              "billNo": "FYS2024000000001"
            }""";
}
