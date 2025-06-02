package com.tekerasoft.tekeramarketplace.repository;

import com.tekerasoft.tekeramarketplace.model.entity.DigitalFashionCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DigitalFashionCategoryRepository extends JpaRepository<DigitalFashionCategory, UUID> {

    boolean existsByName(String name);
}
