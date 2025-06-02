package com.tekerasoft.tekeramarketplace.repository;

import com.tekerasoft.tekeramarketplace.model.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    boolean existsByName(String name);
}
