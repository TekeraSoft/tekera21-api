package com.tekerasoft.tekeramarketplace.model.redisdocument

import com.tekerasoft.tekeramarketplace.model.entity.CartItem
import com.tekerasoft.tekeramarketplace.model.entity.CartStatus
import java.io.Serializable
import java.math.BigDecimal

open class Cart(
    open var id: String,
    open var cartItems: MutableList<CartItem> = mutableListOf(),
    open var totalPrice: BigDecimal,
    open var itemCount: Int,
    open var userId: String
): Serializable



