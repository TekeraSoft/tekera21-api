package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.ExtraDocument
import com.tekerasoft.tekeramarketplace.model.enums.SellerDocument
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToOne

@Entity
open class SellerVerification(

    @OneToOne(fetch = FetchType.LAZY)
    open var sellerUser: User,

    @OneToOne(fetch = FetchType.LAZY)
    open var supervisor: User,

    @OneToOne(fetch = FetchType.LAZY)
    open var seller: Seller,

    open var checkDocumentVerification: Set<SellerDocument>,

    open var extraDocumentVerification: Set<ExtraDocument>,

    open var checkESignature: Boolean,

    ): BaseEntity() {
    constructor(): this(
        User(),
        User(),
        Seller(),
        setOf(),
        setOf(),
        false
    )
}
