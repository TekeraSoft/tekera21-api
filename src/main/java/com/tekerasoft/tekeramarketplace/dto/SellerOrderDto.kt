package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.SellerOrder
import java.math.BigDecimal
import java.time.LocalDateTime

data class SellerOrderDto(
    val id: String?,
    val buyer: BuyerDto,
    val basketItems: MutableList<BasketItemDto>,
    val shippingAddress: AddressDto,
    val billingAddress: AddressDto?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val totalPrice: BigDecimal,
) {
    companion object {
        @JvmStatic
        fun toDto(from: SellerOrder): SellerOrderDto {
            return SellerOrderDto(
                from.id.toString(),
                from.buyer.let { BuyerDto(it.name, it.surname, it.gsmNumber,) },
                from.basketItems.map { it -> BasketItemDto(
                    it.id.toString(),
                    it.name,
                    it.slug,
                    it.code,
                    it.brandName,
                    it.quantity,
                    it.modelCode,
                    it.price,
                    it.sku,
                    it.barcode,
                    it.image,
                    it.attributes,
                    it.shippingPrice,
                    it.shippingCompany.name,
                    it.productId,
                    it.variationId,
                    it.attributeId

                ) }.toMutableList(),
                from.shippingAddress.let { it -> AddressDto.toDto(it) },
                from.billingAddress?.let { it -> AddressDto.toDto(it) },
                from.createdAt,
                from.updatedAt,
                from.totalPrice,
                )
        }
    }
}
