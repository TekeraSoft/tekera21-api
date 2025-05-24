package com.tekerasoft.tekeramarketplace.repository.releational;

import com.tekerasoft.tekeramarketplace.model.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}
