package com.tekerasoft.tekeramarketplace.repository.releational;

import com.tekerasoft.tekeramarketplace.model.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    Page<Product> findActiveProducts(Pageable pageable);

}
