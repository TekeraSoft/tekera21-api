package com.tekerasoft.tekeramarketplace.model.entity

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

    ): BaseEntity() {
    constructor(): this(
        User(),
        User(),
        Seller()
    )
}
