package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
open class BasketItem(
    open var name: String,
    open var slug: String,
    open var code: String,
    open var brandName: String,
    open var quantity: Int,
    open var modelName: String,
    open var modelCode: String,
    open var price: BigDecimal,
    open var sku: String,
    open var barcode: String,
    @ElementCollection
    @CollectionTable(
        name = "basket_item_attributes",
        joinColumns = [JoinColumn(name = "basket_item_id")]
    )
    open var attribute: List<StockAttribute>,
    open var image: String,
    open var companyId: String
): BaseEntity()