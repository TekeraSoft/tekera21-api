package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.redisdocument.Cart
import java.math.BigDecimal

data class CartDto(
    val id: String,
    val cartItems: MutableList<CartItemDto>,
    val totalPrice: BigDecimal,
    val itemCount: Int
) {
    companion object {
        @JvmStatic
        fun toDto(from: Cart): CartDto {
            return CartDto(
                from.id,
                from.cartItems.map { it -> CartItemDto(
                    it.attributeId,
                    it.name,
                    it.quantity,
                    it.price,
                    it.brandName,
                    it.image,
                    it.attributes.map { CartAttributesDto(it.key,it.value) }
                )}.toMutableList(),
                from.totalPrice,
                from.itemCount
            )
        }
    }
}

data class CartItemDto(
    val attributeId: String,
    val name: String,
    val quantity: Int,
    val price: BigDecimal,
    val brandName: String,
    val image: String,
    val attributes: List<CartAttributesDto>
)

data class CartAttributesDto(
    val key: String,
    val value: String,
)
