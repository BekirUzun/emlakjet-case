package com.emlakjet.ecommerce.mapper;

import com.emlakjet.ecommerce.dto.InvoiceDto;
import com.emlakjet.ecommerce.model.Invoice;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.util.CollectionUtils;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class InvoiceMapper {

    public static InvoiceDto toDto(Invoice entity) {
        if (entity == null) {
            return null;
        }
        return InvoiceDto.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .amount(entity.getAmount())
                .productCode(entity.getProductCode())
                .billNo(entity.getBillNo())
                .createdBy(entity.getCreatedBy())
                .isApproved(entity.getIsApproved())
                .build();
    }

    public static Invoice toEntity(InvoiceDto dto) {
        if (dto == null) {
            return null;
        }
        return Invoice.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .amount(dto.getAmount())
                .productCode(dto.getProductCode())
                .billNo(dto.getBillNo())
                .build();
    }

    public static List<InvoiceDto> toDtos(List<Invoice> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return List.of();
        }
        return entities.stream().map(InvoiceMapper::toDto).toList();
    }

    public static List<Invoice> toEntities(List<InvoiceDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return List.of();
        }
        return dtos.stream().map(InvoiceMapper::toEntity).toList();
    }
}
