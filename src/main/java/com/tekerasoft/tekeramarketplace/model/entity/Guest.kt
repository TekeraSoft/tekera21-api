package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.AttributeOverride
import jakarta.persistence.AttributeOverrides
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany

@Entity
open class Guest(
    open var name: String,
    open var surname: String,
    open var email: String,
    open var gsmNumber: String,

    @AttributeOverrides(
        AttributeOverride(name = "city", column = Column(name = "shipping_city")),
        AttributeOverride(name = "street", column = Column(name = "shipping_street")),
        AttributeOverride(name = "postalCode", column = Column(name = "shipping_postal_code")),
        AttributeOverride(name = "buildNo", column = Column(name = "shipping_build_no")),
        AttributeOverride(name = "doorNumber", column = Column(name = "shipping_door_number")),
        AttributeOverride(name = "detailAddress", column = Column(name = "shipping_detail_address")),
        AttributeOverride(name = "country", column = Column(name = "shipping_country")),
    )
    @Embedded
    open var shippingAddress: Address,

    @AttributeOverrides(
        AttributeOverride(name = "city", column = Column(name = "billing_city")),
        AttributeOverride(name = "street", column = Column(name = "billing_street")),
        AttributeOverride(name = "postalCode", column = Column(name = "billing_postal_code")),
        AttributeOverride(name = "buildNo", column = Column(name = "billing_build_no")),
        AttributeOverride(name = "doorNumber", column = Column(name = "billing_door_number")),
        AttributeOverride(name = "detailAddress", column = Column(name = "billing_detail_address")),
        AttributeOverride(name = "country", column = Column(name = "billing_country")),
    )
    @Embedded
    open var billingAddress: Address,

    @OneToMany(fetch = FetchType.LAZY)
    open var orders: MutableList<Order>

): BaseEntity()
