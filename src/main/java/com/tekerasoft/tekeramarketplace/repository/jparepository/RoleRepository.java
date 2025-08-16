package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    Boolean existsByName(String name);
    Role findByName(String name);
}
