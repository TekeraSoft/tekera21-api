package com.tekerasoft.tekeramarketplace.repository.releational;

import com.tekerasoft.tekeramarketplace.model.entity.Variation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VariationRepository extends JpaRepository<Variation, UUID> {
}
