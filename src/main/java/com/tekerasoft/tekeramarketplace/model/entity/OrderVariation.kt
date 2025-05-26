package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Embeddable
import java.math.BigDecimal

@Embeddable
open class OrderVariation(
    open var modelName: String,
    open var modelCode: String,
    open var price: BigDecimal,
    open var sku: String,
    open var barcode: String,
    open var attribute: Attribute,
    open var image: String,

)