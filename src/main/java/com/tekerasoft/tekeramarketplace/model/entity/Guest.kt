package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany

@Entity
open class Guest(
    open var name: String,
    open var surname: String,
    open var email: String,
    open var gsmNumber: String,
    open var shippingAddress: Address,
    open var billingAddress: Address,

    @OneToMany(fetch = FetchType.LAZY)
    open var orders: MutableList<Order>
): BaseEntity()
