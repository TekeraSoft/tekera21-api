package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.SellerVerification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SellerVerificationRepository extends JpaRepository<SellerVerification, UUID> {

    @Query("SELECT sv FROM SellerVerification sv JOIN sv.supervisor su WHERE su.id = :supportId")
    Page<SellerVerification> findBySupportId(@Param("supportId") UUID supportId, Pageable pageable);

    @Query("SELECT sv FROM SellerVerification sv JOIN sv.seller su WHERE su.id = :sellerId")
    SellerVerification findSupervisorVerificationBySupportId(@Param("sellerId") UUID sellerId);

}
