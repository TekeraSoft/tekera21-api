package com.tekerasoft.tekeramarketplace.dto

import java.util.UUID

data class AdminSubCategoryDto(
    val id: UUID?,
    val name: String,
    val slug: String,
    val image: String?,
)
