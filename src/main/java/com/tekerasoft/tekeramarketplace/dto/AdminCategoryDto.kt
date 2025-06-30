package com.tekerasoft.tekeramarketplace.dto

import java.util.UUID

data class AdminCategoryDto(
    val id: UUID?,
    val name: String,
    val slug: String,
    val image: String?
)
