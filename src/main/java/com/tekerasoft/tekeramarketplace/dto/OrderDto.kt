package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Order
import java.math.BigDecimal

data class OrderDto(
    val buyer: BuyerDto,
    val basketItems: MutableList<BasketItemDto>,
    val shippingAddress: AddressDto,
    val billingAddress: AddressDto?,
    val totalPrice: BigDecimal,
) {
    companion object {
        @JvmStatic
        fun toDto(from: Order): OrderDto {
            return OrderDto(
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
                from.totalPrice
            )
        }
    }
}
