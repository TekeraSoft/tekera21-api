package com.tekerasoft.tekeramarketplace.dto.request

import java.math.BigDecimal

data class CreateFabricRequest(
    val fabricName: String,
    val fabricImage: String,
    val fabricPrice: BigDecimal,
    val stock: Int
)
