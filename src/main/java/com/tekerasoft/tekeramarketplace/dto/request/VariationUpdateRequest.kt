package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class VariationUpdateRequest (
    @JsonProperty("id")
    val id: String?=null,

    @JsonProperty("modelName")
    val modelName: String,

    @JsonProperty("modelCode")
    val modelCode: String,

    @JsonProperty("color")
    val color: String,

    @JsonProperty("attributes")
    val attributes: List<AttributeUpdateRequest>,
)