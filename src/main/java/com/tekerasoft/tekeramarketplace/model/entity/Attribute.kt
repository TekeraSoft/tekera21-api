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
    @ElementCollection
    @CollectionTable(
        name = "attributes_stockAttributes",
        joinColumns = [JoinColumn(name = "attribute_id")],
    )
    @JsonProperty("stockAttributes")
    open var stockAttributes: MutableList<StockAttribute> = mutableListOf(),
    @JsonProperty("stock")
    open var stock: Int = 0,

    open var price: BigDecimal = BigDecimal.ZERO,
    open var discountPrice: BigDecimal? = BigDecimal.ZERO,

    open var sku: String,
    open var barcode: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "variation_id")
    open var variation: Variation? = null
): BaseEntity()