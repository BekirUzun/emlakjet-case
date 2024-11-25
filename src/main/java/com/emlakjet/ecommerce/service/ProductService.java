package com.emlakjet.ecommerce.service;

import com.emlakjet.ecommerce.dto.ProductDto;
import com.emlakjet.ecommerce.dto.ProductUpdateDto;
import com.emlakjet.ecommerce.exception.ServiceException;
import com.emlakjet.ecommerce.mapper.ProductMapper;
import com.emlakjet.ecommerce.model.Product;
import com.emlakjet.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.emlakjet.ecommerce.constants.Messages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    /**
     * Creates a new product from the given product data transfer object.
     *
     * @param productDto the product data transfer object
     * @return the created product data transfer object
     * @throws ServiceException if a product with the same code already exists
     */
    public ProductDto create(ProductDto productDto) {
        validateProductCode(productDto.getCode());

        var product = productMapper.toEntity(productDto);
        var savedProduct = productRepository.save(product);
        return productMapper.toDto(savedProduct);
    }

    /**
     * Retrieves a paginated list of products.
     *
     * @param pageable the pagination information
     * @return a page of products
     */
    public Page<ProductDto> list(Pageable pageable) {
        return productRepository.findAll(pageable).map(productMapper::toDto);
    }

    /**
     * Retrieves a product by its code.
     *
     * @param productCode the product code
     * @return an optional containing the product if found, empty otherwise
     */
    public Optional<Product> getByCode(String productCode) {
        return productRepository.findByCode(productCode);
    }

    /**
     * Retrieves a product by its ID and converts it to a ProductDto.
     *
     * @param productId the ID of the product to retrieve
     * @return the product data transfer object if found
     * @throws ServiceException if the product with the given ID does not exist
     */
    public ProductDto getById(String productId) {
        return productMapper.toDto(findById(productId));
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the ID of the product to retrieve
     * @return the product if found
     * @throws ServiceException if the product with the given ID does not exist
     */
    public Product findById(String productId) {
        var productOpt = productRepository.findById(productId);
        if (productOpt.isEmpty()) {
            throw new ServiceException(HttpStatus.NOT_FOUND, PRODUCT_NOT_FOUND);
        }
        return productOpt.get();
    }

    /**
     * Updates a product with the given ID.
     *
     * @param productId the ID of the product to update
     * @param updateDto the dto containing the properties to update
     * @return the updated product data transfer object
     * @throws ServiceException if the product with the given ID does not exist or if no properties are given to update
     */
    public ProductDto update(String productId, ProductUpdateDto updateDto) {
        if (!updateDto.hasPropertiesToUpdate()) {
            throw new ServiceException(HttpStatus.BAD_REQUEST, NO_PROPERTIES_TO_UPDATE);
        }

        var product = findById(productId);
        if (StringUtils.isNotBlank(updateDto.getCode()) && !updateDto.getCode().equals(product.getCode())) {
            validateProductCode(updateDto.getCode());
        }

        var updatedProduct = productMapper.update(product, updateDto);
        var savedProduct = productRepository.save(updatedProduct);
        return productMapper.toDto(savedProduct);
    }

    /**
     * Deletes a product with the given ID.
     *
     * @param productId the ID of the product to delete
     * @throws ServiceException if the product with the given ID does not exist
     */
    public void delete(String productId) {
        productRepository.deleteById(productId);
    }

    /**
     * Checks if a product with the given code already exists.
     *
     * @param productCode the product code to check
     * @throws ServiceException if a product with the same code already exists
     */
    private void validateProductCode(String productCode) {
        var existingProductOpt = getByCode(productCode);
        if (existingProductOpt.isPresent()) {
            throw new ServiceException(HttpStatus.CONFLICT, PRODUCT_WITH_SAME_CODE_EXISTS);
        }
    }

}
