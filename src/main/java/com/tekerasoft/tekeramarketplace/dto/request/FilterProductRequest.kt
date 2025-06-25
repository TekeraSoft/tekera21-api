package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class FilterProductRequest (
    @JsonProperty("color")
    val color: String? = null,
    @JsonProperty("size")
    val clothSize: String? = null,
    @JsonProperty("gender")
    val gender: String? = null
)