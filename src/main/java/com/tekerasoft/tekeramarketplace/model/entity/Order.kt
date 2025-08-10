package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "orders")
open class Order(

    open var orderNo: String,
    open var shippingPrice: BigDecimal,
    open var totalPrice: BigDecimal,
    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    open var sellerOrder: MutableList<SellerOrder>,

    ): BaseEntity()
{
        constructor(): this("", BigDecimal.ZERO, BigDecimal.ZERO,mutableListOf())
}