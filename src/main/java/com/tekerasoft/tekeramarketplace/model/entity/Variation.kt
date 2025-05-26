package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "variations")
open class Variation(
    open var modelName: String,
    open var modelCode: String,
    open var price: BigDecimal,
    open var stock: Int = 0,
    open var sku: String,
    open var barcode: String,

    @ElementCollection
    @CollectionTable(name = "variation_attributes",
        joinColumns = [JoinColumn(name = "variation_id")])
    open var attributes: List<Attribute> = listOf(),

    @ElementCollection
    @CollectionTable(
        name = "variation_images",
        joinColumns = [JoinColumn(name = "variation_id")]
    )
    open var images: List<String>,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    open var product: Product,

    ): BaseEntity()