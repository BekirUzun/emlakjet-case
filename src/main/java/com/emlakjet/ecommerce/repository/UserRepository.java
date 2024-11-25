package com.emlakjet.ecommerce.repository;

import com.emlakjet.ecommerce.model.CommerceUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<CommerceUser, String> {

    Optional<CommerceUser> findByEmail(String email);

}
