package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "orders")
open class Order(
    @OneToMany(mappedBy = "order", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var sellerOrders: MutableList<SellerOrder> = mutableListOf(),

    @Column(unique = true)
    open var orderNo: String = "",

    // Total price, shipping price vb. veriler SellerOrder'lardan türetilebilir
    open var totalPrice: BigDecimal = BigDecimal.ZERO,
    open var shippingPrice: BigDecimal = BigDecimal.ZERO,

    // Yeni eklenen alanlar
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    open var user: User? = null

    // ... Diğer ödeme/sipariş durumları vs.

) : BaseEntity()