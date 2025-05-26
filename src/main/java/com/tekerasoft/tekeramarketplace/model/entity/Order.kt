package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*

@Entity
@Table(name = "orders")
open class Order(

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var basketItems: MutableList<BasketItem>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    open var user: User? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id")
    open var guest: Guest? = null,

    open var paymentType: PaymentType? = null,

    open var shippingAddress: Address,

    open var billingAddress: Address

): BaseEntity()
