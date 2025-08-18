package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.model.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    Order findByOrderNo(String orderNo);

    @Query("""
    SELECT DISTINCT o
    FROM Order o
    JOIN o.sellerOrder so
    JOIN so.basketItems bi
    JOIN bi.seller s
    JOIN s.users u
    WHERE u.id = :userId
    ORDER BY o.createdAt DESC
""")
    Page<Order> getSellerOrdersByUserId(@Param("userId") UUID userId, Pageable pageable);
}
