package com.tekerasoft.tekeramarketplace.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Embeddable

@Embeddable
data class StockAttribute(
    @JsonProperty("key")
    val key: String = "",
    @JsonProperty("value")
    val value: String = "",
)
