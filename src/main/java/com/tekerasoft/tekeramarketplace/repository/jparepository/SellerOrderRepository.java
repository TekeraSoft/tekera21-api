package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.dto.SellerReportAggregation;
import com.tekerasoft.tekeramarketplace.model.entity.Order;
import com.tekerasoft.tekeramarketplace.model.entity.Seller;
import com.tekerasoft.tekeramarketplace.model.entity.SellerOrder;
import com.tekerasoft.tekeramarketplace.model.entity.User;
import com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
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
        ORDER BY o.createdAt DESC
    """)
        Page<SellerOrder> findUserOrdersByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
    SELECT DISTINCT so
    FROM SellerOrder so
    JOIN so.basketItems bi
    JOIN bi.seller s
    JOIN s.users u
    WHERE u.id = :userId
    AND so.paymentStatus = com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus.PAID
    ORDER BY so.createdAt DESC
""")
    Page<SellerOrder> findSellerOrdersByUserId(@Param("userId") UUID userId, Pageable pageable);


    @Query("SELECT so FROM SellerOrder so JOIN so.basketItems bi " +
            "WHERE bi.seller.id = :sellerId " +
            "AND so.paymentStatus = :status")
    Optional<SellerOrder> findPendingOrderBySeller(@Param("sellerId") UUID sellerId,
                                                   @Param("status") PaymentStatus status);


    Page<SellerOrder> findBySeller(Seller seller, Pageable pageable);

}


