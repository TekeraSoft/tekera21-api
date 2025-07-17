package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.ShippingCompany;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ShippingCompanyRepository extends JpaRepository<ShippingCompany, UUID> {
}
