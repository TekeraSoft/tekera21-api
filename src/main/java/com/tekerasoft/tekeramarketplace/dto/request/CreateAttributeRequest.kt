package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import java.math.BigDecimal

data class CreateAttributeRequest(
    @JsonProperty("attributeDetails")
    val attributeDetails: MutableList<AttributeDetail>,
    @JsonProperty("stock")
    val stock: Int,
    @JsonProperty("maxPurchaseStock")
    val maxPurchaseStock: Int,
    @JsonProperty("price")
    val price: BigDecimal,
    @JsonProperty("discountPrice")
    val discountPrice: BigDecimal,
    @JsonProperty("vatPercent")
    val vatPercent: BigDecimal,
    @JsonProperty("sku")
    val sku: String,
    @JsonProperty("barcode")
    val barcode: String
)
