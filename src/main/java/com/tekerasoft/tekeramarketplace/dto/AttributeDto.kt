package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.StockAttribute
import java.math.BigDecimal

data class AttributeDto(
    val stockAttribute: MutableList<StockAttribute>,
    val stock: Int,
    val price: BigDecimal,
    val discountPrice: BigDecimal?,
    val sku: String,
    val barcode: String,
)
