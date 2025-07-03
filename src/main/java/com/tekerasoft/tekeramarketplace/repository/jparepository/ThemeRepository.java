package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.Theme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ThemeRepository extends JpaRepository<Theme, UUID> {
}
