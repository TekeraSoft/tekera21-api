package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity
import java.math.BigDecimal


open class Cart(
    open var basketItems: MutableList<BasketItem>,
    open var totalPrice: BigDecimal,
    open var itemCount: Int,
    open var userId: String
)