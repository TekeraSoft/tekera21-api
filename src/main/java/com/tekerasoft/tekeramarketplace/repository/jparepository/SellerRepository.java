package com.tekerasoft.tekeramarketplace.repository.jparepository;

import com.tekerasoft.tekeramarketplace.dto.SellerReportAggregation;
import com.tekerasoft.tekeramarketplace.model.entity.Seller;
import com.tekerasoft.tekeramarketplace.model.entity.Product;
import com.tekerasoft.tekeramarketplace.model.entity.SellerDocument;
import com.tekerasoft.tekeramarketplace.model.enums.SellerDocumentType;
import com.tekerasoft.tekeramarketplace.model.enums.VerificationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface SellerRepository extends JpaRepository<Seller, UUID> {

    @Query("SELECT p FROM Seller p WHERE p.isActive = true")
    Page<Seller> findActiveSeller(Pageable pageable);

    @Query("""
    SELECT d
    FROM Seller s
    JOIN s.sellerDocuments d
    WHERE s.id = :sellerId
      AND d.verificationStatus <> :verifiedStatus
""")
    List<SellerDocument> findUnverifiedDocumentsBySeller(
            @Param("sellerId") UUID sellerId,
            @Param("verifiedStatus") VerificationStatus verifiedStatus
    );

    Page<Product> findById(UUID id, Pageable pageable);

    boolean existsByNameAndTaxNumber(String name, String taxNumber);

    @Query("SELECT s FROM Seller s JOIN s.users u WHERE u.id = :userId")
    Seller findSellerByUserId(@Param("userId") UUID userId);


    @Query("SELECT p FROM Seller s " +
            "JOIN s.users u " +
            "JOIN s.products p " +
            "WHERE u.id = :userId")
    Page<Product> findProductsByUserId(@Param("userId") UUID userId,Pageable pageable);

    @Query(
            "SELECT new com.tekerasoft.tekeramarketplace.dto.SellerReportAggregation(" +
                    // Günlük
                    "COALESCE(SUM(CASE WHEN FUNCTION('date_trunc','day', so.createdAt) = FUNCTION('date_trunc','day', CURRENT_TIMESTAMP) THEN bi.price ELSE NULL END), 0), " +
                    // Haftalık (ISO week, Pazartesi başlangıç)
                    "COALESCE(SUM(CASE WHEN FUNCTION('date_trunc','week', so.createdAt) = FUNCTION('date_trunc','week', CURRENT_TIMESTAMP) THEN bi.price ELSE NULL END), 0), " +
                    // Aylık
                    "COALESCE(SUM(CASE WHEN FUNCTION('date_trunc','month', so.createdAt) = FUNCTION('date_trunc','month', CURRENT_TIMESTAMP) THEN bi.price ELSE NULL END), 0)) " +
                    "FROM Seller s " +
                    "JOIN s.sellerOrders so " +
                    "JOIN so.basketItems bi " +
                    "WHERE s.id = :sellerId"
    )
    SellerReportAggregation getSellerAggregatedProfit(@Param("sellerId") java.util.UUID sellerId);

    @Query("SELECT SUM(bi.price) " +
            "FROM Seller s " +
            "JOIN s.sellerOrders so " +
            "JOIN so.basketItems bi " +
            "WHERE s.id = :sellerId " +
            "AND FUNCTION('DATE', so.createdAt) = :specificDate")
    BigDecimal getProfitBySpecificDate(@Param("sellerId") Long sellerId,
                                       @Param("specificDate") java.time.LocalDate specificDate);
}