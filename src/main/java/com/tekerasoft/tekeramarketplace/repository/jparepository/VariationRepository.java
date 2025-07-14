package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.Variation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface VariationRepository extends JpaRepository<Variation, UUID>{

    @Query(value = """
    SELECT v.* 
    FROM variations v 
    JOIN variation_images vi ON v.id = vi.variation_id 
    WHERE vi.images = :imagePath
""", nativeQuery = true)
    Optional<Variation> findByImagePath(@Param("imagePath") String imagePath);

}
