package com.tekerasoft.tekeramarketplace.dto

import java.util.UUID

data class ProductCompanyDto(
    val id: UUID?,
    val name: String,
    val logo: String,
    val rate: Double
)
