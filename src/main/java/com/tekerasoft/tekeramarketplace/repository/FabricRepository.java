package com.tekerasoft.tekeramarketplace.repository;

import com.tekerasoft.tekeramarketplace.model.entity.Fabric;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FabricRepository extends JpaRepository<Fabric, UUID> {
}
