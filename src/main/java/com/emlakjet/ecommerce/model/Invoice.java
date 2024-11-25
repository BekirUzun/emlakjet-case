package com.emlakjet.ecommerce.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Setter
@Getter
@SuperBuilder
@ToString
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class Invoice extends BaseEntity {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Double amount;
    private String productCode;
    private String billNo;
    private Boolean isApproved;
}
