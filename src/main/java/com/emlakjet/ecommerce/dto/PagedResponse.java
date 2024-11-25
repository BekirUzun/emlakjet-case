package com.emlakjet.ecommerce.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;


@Data
public class PagedResponse<T> {
    List<T> content;
    CustomPageable pageable;

    public PagedResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageable = CustomPageable.builder()
                .pageNumber(page.getPageable().getPageNumber())
                .pageSize(page.getPageable().getPageSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Data
    @Builder
    static class CustomPageable {
        int pageNumber;
        int pageSize;
        long totalElements;
        int totalPages;
    }
}
