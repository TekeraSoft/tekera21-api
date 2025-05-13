package com.tekerasoft.tekeramarketplace.repository;

import com.tekerasoft.tekeramarketplace.model.Variation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VariationRepository extends JpaRepository<Variation, UUID> {
}
