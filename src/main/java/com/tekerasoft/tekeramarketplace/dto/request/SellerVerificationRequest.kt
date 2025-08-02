package com.tekerasoft.tekeramarketplace.dto.request

data class SellerVerificationRequest(
    val userId: String,
    val documentsVerificationRequest: Boolean,

)
