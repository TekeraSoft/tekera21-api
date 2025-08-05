package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.ExtraDocument
import com.tekerasoft.tekeramarketplace.model.enums.SellerDocument
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToOne

@Entity
open class SellerVerification(

    @OneToOne(fetch = FetchType.LAZY)
    open var sellerUser: User,

    @OneToOne(fetch = FetchType.LAZY)
    open var supervisor: User,

    @OneToOne(fetch = FetchType.LAZY)
    open var seller: Seller,

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "seller_verification_documents", joinColumns = [JoinColumn(name = "seller_verification_id")])
    @Enumerated(EnumType.STRING)
    open var checkDocumentVerification: MutableSet<SellerDocument> = mutableSetOf(),

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "seller_verification_extra_documents", joinColumns = [JoinColumn(name = "seller_verification_id")])
    @Enumerated(EnumType.STRING)
    open var extraDocumentVerification: MutableSet<ExtraDocument> = mutableSetOf(),

    open var checkESignature: Boolean,

    ): BaseEntity() {
    constructor(): this(
        User(),
        User(),
        Seller(),
        mutableSetOf(),
        mutableSetOf(),
        false
    )
}
