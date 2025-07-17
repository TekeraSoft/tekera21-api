package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    @Query("SELECT o FROM Order o WHERE o.company.id = :companyId")
    List<Order> findOrdersByCompanyId(@Param("companyId") String companyId);

    @Query("""
    SELECT o FROM Order o
    WHERE LOWER(o.buyer.gsmNumber) = LOWER(:searchParam)
       OR LOWER(o.buyer.name) = LOWER(:searchParam)
       OR LOWER(o.buyer.surname) = LOWER(:searchParam)
       OR LOWER(CONCAT(o.buyer.name, ' ', o.buyer.surname)) = LOWER(:searchParam)
    """)
    List<Order> findOrdersByPhoneNumberOrUsername(@Param("searchParam") String searchParam);
}
