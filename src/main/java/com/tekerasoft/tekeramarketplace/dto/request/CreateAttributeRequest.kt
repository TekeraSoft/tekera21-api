package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.StockAttribute

data class CreateAttributeRequest(
    @JsonProperty("stockAttribute")
    val stockAttribute: MutableList<StockAttribute>,
    @JsonProperty("stock")
    val stock: Int,
)
