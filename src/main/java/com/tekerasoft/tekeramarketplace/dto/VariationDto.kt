package com.tekerasoft.tekeramarketplace.dto

import java.util.UUID

data class VariationDto(
    val id: UUID?,
    val modelName: String,
    val modelCode: String,
    val attributes: List<AttributeDto>,
    val images: List<String>?,
)
