package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne

@Entity
open class SellerVerification(

    @ManyToOne(fetch = FetchType.LAZY)
    open var sellerUser: User,

    @ManyToOne(fetch = FetchType.LAZY)
    open var supervisor: User,

    @ManyToOne(fetch = FetchType.LAZY)
    open var seller: Seller,

    ): BaseEntity() {
    constructor(): this(
        User(),
        User(),
        Seller()
    )
}
