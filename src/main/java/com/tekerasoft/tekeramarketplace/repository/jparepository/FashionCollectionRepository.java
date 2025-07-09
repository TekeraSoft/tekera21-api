package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.FashionCollection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.UUID;

public interface FashionCollectionRepository extends JpaRepository<FashionCollection, UUID> {
    @Query("SELECT fc FROM FashionCollection fc WHERE fc.isActive = true")
    Page<FashionCollection> findActiveCollections(Pageable pageable);
}
