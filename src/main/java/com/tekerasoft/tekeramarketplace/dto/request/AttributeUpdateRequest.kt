package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.StockAttribute
import java.math.BigDecimal

data class AttributeUpdateRequest(
    @JsonProperty("stockAttribute")
    val stockAttribute: MutableList<StockAttribute>,
    @JsonProperty("stock")
    val stock: Int,
    @JsonProperty("price")
    val price: BigDecimal,
    @JsonProperty("discountPrice")
    val discountPrice: BigDecimal,
    @JsonProperty("sku")
    val sku: String,
    @JsonProperty("barcode")
    val barcode: String
)
