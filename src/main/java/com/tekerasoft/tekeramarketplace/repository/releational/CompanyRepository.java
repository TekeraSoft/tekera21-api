package com.tekerasoft.tekeramarketplace.repository.releational;

import com.tekerasoft.tekeramarketplace.model.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {
}
