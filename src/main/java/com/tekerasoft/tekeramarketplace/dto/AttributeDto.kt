package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import java.math.BigDecimal

data class AttributeDto(
    val attributeDetails: List<AttributeDetail>,
    val stock: Int,
    val price: BigDecimal,
    val discountPrice: BigDecimal?,
    val sku: String?,
    val barcode: String?,
)
