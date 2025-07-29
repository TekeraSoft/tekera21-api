package com.tekerasoft.tekeramarketplace.model.entity

import java.math.BigDecimal
import javax.persistence.Embeddable
import javax.persistence.Entity

@Entity
open class CartItem(
    open var attributeId: String,
    open var name: String,
    open var quantity: Int,
    open var price: BigDecimal,
    open var brandName: String,
    open var image: String,
    open var attributes: List<CartAttributes>
)

@Embeddable
open class CartAttributes(
    open var key: String,
    open var value: String,
)
