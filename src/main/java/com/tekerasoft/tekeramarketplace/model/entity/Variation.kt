package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "variations")
open class Variation(
    open var modelName: String,
    open var modelCode: String,
    open var price: BigDecimal = BigDecimal.ZERO,
    open var discountPrice: BigDecimal? = BigDecimal.ZERO,
    open var sku: String,
    open var barcode: String,

    @OneToMany(mappedBy="variation",fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var attributes: MutableList<Attribute> = mutableListOf(),

    @ElementCollection
    @CollectionTable(
        name = "variation_images",
        joinColumns = [JoinColumn(name = "variation_id")]
    )
    open var images: List<String> = listOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    open var product: Product,

    ): BaseEntity()