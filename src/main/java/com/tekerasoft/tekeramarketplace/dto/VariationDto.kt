package com.tekerasoft.tekeramarketplace.dto

import java.math.BigDecimal

data class VariationDto(
    val modelName:String,
    val modelCode: String,
    val price: BigDecimal,
    val stock: Int,
    val sku: String,
    val barcode: String,
    val attributes: List<AttributeDto>,
    val images: List<String>,
)
