package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany
import java.math.BigDecimal

@Entity
open class Cart(

    @OneToMany(fetch = FetchType.LAZY)
    open var basketItems: MutableList<BasketItem>,

    open var totalPrice: BigDecimal,

    open var itemCount: Int,

    open var userId: String,

    open var cartStatus: CartStatus

): BaseEntity()