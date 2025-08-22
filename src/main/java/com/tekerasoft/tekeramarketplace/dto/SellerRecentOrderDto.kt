package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.SellerOrder
import java.math.BigDecimal
import java.time.LocalDateTime

data class SellerRecentOrderDto(
    val id: String,
    val orderNumber: String?,
    val buyerFirstName: String,
    val buyerLastName: String,
    val totalPrice: BigDecimal,
    val shippingPrice: BigDecimal,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val productImage: List<String>
) {
    companion object {
        @JvmStatic
        fun toDto(from: SellerOrder): SellerRecentOrderDto {
            return SellerRecentOrderDto(
                from.id.toString(),
                from.order?.orderNo,
                from.buyer.name,
                from.buyer.surname,
                from.totalPrice,
                from.shippingPrice,
                from.createdAt,
                from.updatedAt,
                from.basketItems.map { it -> it.image }
            )
        }
    }
}
