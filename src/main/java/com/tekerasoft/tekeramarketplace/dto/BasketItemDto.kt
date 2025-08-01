package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.BasketAttributes
import java.math.BigDecimal

data class BasketItemDto(
    val id: String,
    val name: String,
    val slug: String,
    val code: String?,
    val brandName: String,
    val quantity: Int,
    val modelCode: String?,
    val price: BigDecimal,
    val sku: String?,
    val barcode: String?,
    val image: String,
    val attributes: MutableList<BasketAttributes>,
    val shippingPrice: BigDecimal,
    val shippingCompanyName: String,
    val productId: String,
    val variationId: String,
    val attributeId: String
)
