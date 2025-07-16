package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "orders")
open class Order(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    open var user: User? = null,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    open var buyer: Buyer,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var basketItems: MutableList<BasketItem>,

    @OneToOne(fetch = FetchType.LAZY)
    open var shippingAddress: Address,

    @OneToOne(fetch = FetchType.LAZY)
    open var billingAddress: Address? = null,

    open var totalPrice : BigDecimal,

    open var shippingPrice: BigDecimal,

    open var paymentType: PaymentType? = null,

    open var paymentStatus: PaymentStatus? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shipping_company_id")
    open var shippingCompany: ShippingCompany,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    open var company: Company? = null,

): BaseEntity()
