package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.Attribute
import java.math.BigDecimal

data class VariationRequest(
    @JsonProperty("modelName")
    val modelName: String,
    @JsonProperty("modelCode")
    val modelCode: String,
    @JsonProperty("price")
    val price: BigDecimal,
    @JsonProperty("stock")
    val stock: Int,
    @JsonProperty("sku")
    val sku: String,
    @JsonProperty("barcode")
    val barcode: String,
    @JsonProperty("attributes")
    val attributes: List<Attribute>,
    @JsonProperty("images")
    val images: List<String>
)
