package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.Attribute;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    @Modifying
    @Query("UPDATE Attribute a SET a.stock = a.stock - :quantity WHERE a.id = :attributeId AND a.stock >= :quantity")
    void decreaseStock(@Param("attributeId")UUID attributeId, @Param("quantity")Integer quantity);
}
