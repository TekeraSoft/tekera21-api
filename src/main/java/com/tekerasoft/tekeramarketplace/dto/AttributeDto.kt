package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import java.math.BigDecimal
import java.util.UUID

data class AttributeDto(
    val id: UUID?,
    val attributeDetails: List<AttributeDetail>,
    val stock: Int,
    val maxPurchaseStock: Int?,
    val price: BigDecimal,
    val discountPrice: BigDecimal?,
    val sku: String?,
    val barcode: String?,
)
