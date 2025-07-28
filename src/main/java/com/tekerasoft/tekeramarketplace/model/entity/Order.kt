package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "orders")
open class Order(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    open var user: User? = null,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "buyer_id")
    open var buyer: Buyer,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var basketItems: MutableList<BasketItem>,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var shippingAddress: Address,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var billingAddress: Address? = null,

    open var totalPrice : BigDecimal,

    open var shippingPrice: BigDecimal,

    open var paymentType: PaymentType? = null,

    open var paymentStatus: PaymentStatus? = null,


): BaseEntity()
