package com.tekerasoft.tekeramarketplace.dto.request

import com.tekerasoft.tekeramarketplace.model.entity.CartAttributes
import java.math.BigDecimal

data class AddToCartRequest(
    val basketItems: List<CartItem>,
    val totalPrice: BigDecimal,
    val itemCount: Int,
    val userId: String
)

open class CartItem(
    open var attributeId: String,
    open var name: String,
    open var quantity: Int,
    open var price: BigDecimal,
    open var brandName: String,
    open var image: String,
    open var attributes: List<CartAttributes>
)
