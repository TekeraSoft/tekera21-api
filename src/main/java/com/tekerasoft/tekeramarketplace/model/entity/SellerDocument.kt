package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.VerificationStatus
import jakarta.persistence.Embeddable

@Embeddable
data class SellerDocument(
    val documentTitle: String,
    val documentPath: String,
    val verificationStatus: VerificationStatus = VerificationStatus.PENDING,
)
