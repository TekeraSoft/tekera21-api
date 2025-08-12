package com.tekerasoft.tekeramarketplace.dto.response

data class SellerVerifiedResponse(
    val message: String,
    val notVerifiedDocuments: List<String>? = null,
)
