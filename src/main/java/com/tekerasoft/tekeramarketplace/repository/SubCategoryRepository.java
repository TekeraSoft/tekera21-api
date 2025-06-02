package com.tekerasoft.tekeramarketplace.repository;

import com.tekerasoft.tekeramarketplace.model.entity.SubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SubCategoryRepository extends JpaRepository<SubCategory, UUID> {
    Boolean existsByName(String name);
}
