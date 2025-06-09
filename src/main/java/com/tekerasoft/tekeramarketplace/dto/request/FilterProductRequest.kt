package com.tekerasoft.tekeramarketplace.dto.request

data class FilterProductRequest (
    val modelName: String?,
    val attributes: Map<String, String>?
)