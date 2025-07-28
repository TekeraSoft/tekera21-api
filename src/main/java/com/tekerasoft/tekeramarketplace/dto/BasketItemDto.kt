package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.BasketAttributes
import java.math.BigDecimal

data class BasketItemDto(
    val name: String,
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
)
