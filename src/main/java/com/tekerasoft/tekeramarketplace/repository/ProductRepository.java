package com.tekerasoft.tekeramarketplace.repository;

import com.tekerasoft.tekeramarketplace.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    Page<Product> findActiveProducts(Pageable pageable);

    Optional<Product> findBySlug(String slug);

}
