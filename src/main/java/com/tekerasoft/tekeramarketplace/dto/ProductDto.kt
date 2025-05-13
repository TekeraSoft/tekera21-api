package com.tekerasoft.tekeramarketplace.dto

data class ProductDto(
    val name: String,
    val slug: String,
    val code: String,
    val description: String,
    val category: List<CategoryDto>
)
