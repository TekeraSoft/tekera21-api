package com.tekerasoft.tekeramarketplace.repository.jparepository;

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

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.company.id = :companyId")
    Page<Product> findActiveProductsByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.company.id = :companyId AND p.slug = :slug")
    Optional<Product> findActiveProductByCompanyIdAndSlug(@Param("companyId") UUID companyId, @Param("slug") String slug);

    @Query("""
    SELECT DISTINCT p FROM Product p
    JOIN p.variations v
    JOIN v.attributes a
    JOIN a.attributeDetails ad
    JOIN p.tags t
    WHERE (:color IS NULL OR v.color = :color)
      AND (:size IS NULL OR (ad.key = 'size' AND ad.value = :size))
      AND (:style IS NULL OR (ad.key = 'style' AND ad.value = : style))
      AND (:tag IS NULL OR t = :tag)
""")
    Page<Product> findByQueryField(@Param("color") String color,
                                   @Param("size") String size,
                                   @Param("tag") String tag,
                                   @Param("tag") String style,
                                   Pageable pageable);

    Optional<Product> findBySlug(String slug);

}
