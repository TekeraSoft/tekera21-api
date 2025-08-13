package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.SellerDocumentType
import com.tekerasoft.tekeramarketplace.model.enums.VerificationStatus
import jakarta.persistence.Embeddable
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated

@Embeddable
open class SellerDocument(
    @Enumerated(EnumType.STRING)
    open var documentTitle: SellerDocumentType?,
    open var documentPath: String? = null,
    open var verificationStatus: VerificationStatus = VerificationStatus.PENDING,
    open var faultyDocumentDescription: String? = null,
) {
    constructor(): this(null, "", VerificationStatus.PENDING)
}