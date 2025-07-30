package com.tekerasoft.tekeramarketplace.model.redisdocument

import java.io.Serializable
import java.math.BigDecimal

open class Cart(
    open var id: String,
    open var cartItems: MutableList<CartItem> = mutableListOf(),
    open var totalPrice: BigDecimal,
    open var itemCount: Int,
    open var userId: String,
    open var cartStatus: CartStatus = CartStatus.WAITING
): Serializable

open class CartItem(
    open var attributeId: String,
    open var name: String,
    open var quantity: Int,
    open var price: BigDecimal,
    open var brandName: String,
    open var image: String,
    open var attributes: MutableList<CartAttributes>
): Serializable

enum class CartStatus { WAITING, COMPLETED }

data class CartAttributes(
    val key: String,
    val value: String
) : Serializable

