package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*

@Entity
open class BasketItem(

    open var name: String,
    open var slug: String,
    open var code: String,
    open var brandName: String,

    @Embedded
    open var variation: OrderVariation,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    open var company: Company

): BaseEntity()
