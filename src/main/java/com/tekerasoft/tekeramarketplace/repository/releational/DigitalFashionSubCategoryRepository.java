package com.tekerasoft.tekeramarketplace.repository.releational;

import com.tekerasoft.tekeramarketplace.model.entity.DigitalFashionSubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DigitalFashionSubCategoryRepository extends JpaRepository<DigitalFashionSubCategory, UUID> {
}
