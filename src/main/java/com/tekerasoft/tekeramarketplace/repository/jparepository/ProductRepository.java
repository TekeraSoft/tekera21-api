package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.Product;
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

    @Query("SELECT p FROM Product p WHERE p.isActive = true")
    Page<Product> findActiveProducts(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.company.id = :companyId")
    Page<Product> findActiveProductsByCompanyId(@Param("companyId") UUID companyId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.isActive = true AND p.company.id = :companyId AND p.slug = :slug")
    Product findActiveProductByCompanyIdAndSlug(@Param("companyId") UUID companyId, @Param("slug") String slug);

    Optional<Product> findBySlug(String slug);

}
