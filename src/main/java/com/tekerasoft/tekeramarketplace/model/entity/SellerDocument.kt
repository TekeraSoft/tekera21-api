package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.SellerDocument
import com.tekerasoft.tekeramarketplace.model.enums.VerificationStatus
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
open class SellerDocument(
    @Enumerated(EnumType.STRING)
    open var documentTitle: SellerDocument?,
    open var documentPath: String,
    open var verificationStatus: VerificationStatus = VerificationStatus.PENDING,
) {
    constructor(): this(null, "", VerificationStatus.PENDING)
}