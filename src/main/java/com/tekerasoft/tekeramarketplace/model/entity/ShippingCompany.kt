package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name="shipping_company")
open class ShippingCompany(
    open var name: String,
    open var gsmNumber: String,
    open var email: String,
    open var price: BigDecimal = BigDecimal.ZERO,
    open var minDeliveryDay: Int,
    open var maxDeliveryDay: Int,

    @OneToMany(fetch = FetchType.LAZY)
    open var orders: MutableList<Order> = mutableListOf(),

    @ManyToMany(mappedBy = "shippingCompanies", fetch = FetchType.LAZY)
    open var companies: MutableSet<Company> = mutableSetOf(),

): BaseEntity()
