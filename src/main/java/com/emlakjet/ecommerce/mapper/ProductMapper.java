package com.emlakjet.ecommerce.mapper;

import com.emlakjet.ecommerce.dto.ProductDto;
import com.emlakjet.ecommerce.dto.ProductUpdateDto;
import com.emlakjet.ecommerce.model.Product;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
public class ProductMapper {

    public ProductDto toDto(Product entity) {
        if (entity == null) {
            return null;
        }
        return ProductDto.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .name(entity.getName())
                .description(entity.getDescription())
                .price(entity.getPrice())
                .createdBy(entity.getCreatedBy())
                .build();
    }

    public Product toEntity(ProductDto dto) {
        if (dto == null) {
            return null;
        }
        return Product.builder()
                .id(dto.getId())
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .build();
    }

    public List<ProductDto> toDtos(List<Product> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return List.of();
        }
        return entities.stream().map(this::toDto).toList();
    }

    public List<Product> toEntities(List<ProductDto> dtos) {
        if (CollectionUtils.isEmpty(dtos)) {
            return List.of();
        }
        return dtos.stream().map(this::toEntity).toList();
    }

    public Product update(Product entity, ProductUpdateDto updateDto) {
        if (updateDto == null) {
            return entity;
        }
        if (StringUtils.isNotBlank(updateDto.getCode())) {
            entity.setCode(updateDto.getCode());
        }
        if (StringUtils.isNotBlank(updateDto.getName())) {
            entity.setName(updateDto.getName());
        }
        if (StringUtils.isNotBlank(updateDto.getDescription())) {
            entity.setDescription(updateDto.getDescription());
        }
        if (updateDto.getPrice() != null) {
            entity.setPrice(updateDto.getPrice());
        }
        return entity;
    }
}
