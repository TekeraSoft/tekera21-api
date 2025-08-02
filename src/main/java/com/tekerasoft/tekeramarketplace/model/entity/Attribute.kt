package com.tekerasoft.tekeramarketplace.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name = "attributes")
open class Attribute(
    open var price: BigDecimal = BigDecimal.ZERO,
    open var discountPrice: BigDecimal? = BigDecimal.ZERO,
    open var stock: Int,
    open var sku: String? = null,
    open var barcode: String? = null,

    @ElementCollection
    @CollectionTable(
        name = "attributes_stockAttributes",
        joinColumns = [JoinColumn(name = "attribute_id")],
    )
    open var attributeDetails: MutableList<AttributeDetail> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id")
    open var variation: Variation? = null,

    ): BaseEntity() {
        constructor() : this(
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            0,
            "",
            "",
            mutableListOf(),
            Variation()
        )
    }