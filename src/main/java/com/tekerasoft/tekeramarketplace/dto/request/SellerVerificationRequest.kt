package com.tekerasoft.tekeramarketplace.dto.request

import com.tekerasoft.tekeramarketplace.model.entity.SellerDocument
import com.tekerasoft.tekeramarketplace.model.enums.ExtraDocument

data class SellerVerificationRequest(
    val userId: String,
    val supervisorId: String,
    val sellerId: String,
    val documentsVerificationRequest: SellerDocument,
    val extraDocument: ExtraDocument,
    val eSignature: Boolean,
)
