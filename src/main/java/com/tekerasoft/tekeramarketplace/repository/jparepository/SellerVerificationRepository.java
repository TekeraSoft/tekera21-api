package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.SellerVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SellerVerificationRepository extends JpaRepository<SellerVerification, UUID> {
}
