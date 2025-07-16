package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.Product;
import com.tekerasoft.tekeramarketplace.model.entity.Variation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    @Query("""
       SELECT p
       FROM Product p
       WHERE p.isActive = true
       ORDER BY p.createdAt DESC, p.id DESC
       """)
    Page<Product> findActiveProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.company.id = :companyId")
    Page<Product> findActiveProductsByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE :tag MEMBER OF p.tags AND p.company.id = :companyId")
    Page<Product> findPopularOrNewSeasonProducts(@Param("companyId") UUID companyId,
                                                 @Param("tag") String tag,
                                                 Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.company.id = :companyId AND p.slug = :slug")
    Optional<Product> findActiveProductByCompanyIdAndSlug(@Param("companyId") UUID companyId, @Param("slug") String slug);

    @Query("""
   SELECT DISTINCT p
     FROM Product p
     JOIN p.variations v
     JOIN v.attributes a
     JOIN a.attributeDetails ad
     JOIN p.tags t
     JOIN p.subCategories sc
    WHERE p.isActive = TRUE
      AND (:subCategoryName IS NULL OR sc.name = :subCategoryName)
      AND (:color IS NULL OR v.color = :color)
      AND (:size  IS NULL OR (ad.key = 'size'  AND ad.value = :size))
      AND (:style IS NULL OR (ad.key = 'style' AND ad.value = :style))
      AND (:tags  IS NULL OR t IN :tags)
""")
    Page<Product> findByQueryField(@Param("color")  String color,
                                   @Param("size")   String size,
                                   @Param("tags") List<String> tags,  // <‑‑ List!
                                   @Param("style")  String style,
                                   @Param("subCategoryName") String subCategoryName,
                                   Pageable pageable);

    Optional<Product> findBySlug(String slug);

    @Query("SELECT p FROM Product p JOIN p.subCategories sc WHERE sc.name = :name")
    Page<Product> findProductBySubCategory(@Param("name") String name, Pageable pageable);

}
