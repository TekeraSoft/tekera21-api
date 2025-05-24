package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Embeddable

@Embeddable
data class CompanyDocument(
    val documentTitle: String,
    val documentPath: String,
    val verificationStatus: VerificationStatus = VerificationStatus.PENDING,
)
