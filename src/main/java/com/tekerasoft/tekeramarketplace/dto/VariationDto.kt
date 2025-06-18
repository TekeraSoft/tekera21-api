package com.tekerasoft.tekeramarketplace.dto

import java.math.BigDecimal
import java.util.UUID

data class VariationDto(
    val id: UUID?,
    val modelName: String,
    val modelCode: String,
    val price: BigDecimal,
    val discountPrice: BigDecimal,
    val stock: Int,
    val sku: String,
    val barcode: String,
    val attributes: List<AttributeDto>,
    val images: List<String>?,
)
