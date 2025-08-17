package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.dto.SellerReportAggregation;
import com.tekerasoft.tekeramarketplace.model.entity.Order;
import com.tekerasoft.tekeramarketplace.model.entity.SellerOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SellerOrderRepository extends JpaRepository<SellerOrder, UUID> {

    @Query("""
    SELECT o FROM SellerOrder o
    WHERE LOWER(o.buyer.gsmNumber) = LOWER(:searchParam)
       OR LOWER(o.buyer.name) = LOWER(:searchParam)
       OR LOWER(o.buyer.surname) = LOWER(:searchParam)
       OR LOWER(CONCAT(o.buyer.name, ' ', o.buyer.surname)) = LOWER(:searchParam)
    """)
    List<SellerOrder> findOrdersByPhoneNumberOrUsername(@Param("searchParam") String searchParam);

    @Query("""
    SELECT o
    FROM SellerOrder o
    WHERE o.user.id = :userId
    """)
        Page<SellerOrder> findOrderByUserId(@Param("userId") UUID userId, Pageable pageable);


    @Query("""
    SELECT DISTINCT so
    FROM SellerOrder so
    JOIN so.basketItems bi
    JOIN bi.seller s
    JOIN s.users u
    WHERE u.id = :userId
    AND so.paymentStatus = com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus.PAID
""")
    Page<SellerOrder> findSellerOrdersByUserId(@Param("userId") UUID userId, Pageable pageable);
}


