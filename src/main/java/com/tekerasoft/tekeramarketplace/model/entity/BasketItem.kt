package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity
open class BasketItem(
    @Id
    open var id: UUID,
    open var name: String,
    open var code: String? = null,
    open var brandName: String,
    open var quantity: Int,
    open var modelCode: String? = null,
    open var price: BigDecimal,
    open var sku: String? = null,
    open var barcode: String? = null,
    open var image: String,

    open var attributeId: String,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var attributes: MutableList<BasketAttributes>,

    open var shippingPrice: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    open var company: Company,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_company_id")
    open var shippingCompany: ShippingCompany,

    )