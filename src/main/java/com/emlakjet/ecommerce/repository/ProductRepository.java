package com.emlakjet.ecommerce.repository;

import com.emlakjet.ecommerce.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, String> {

    Page<Product> findAll(Pageable pageable);

    Optional<Product> findByCode(String code);
}
