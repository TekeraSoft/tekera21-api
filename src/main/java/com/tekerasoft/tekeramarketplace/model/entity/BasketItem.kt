package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*

@Entity
open class BasketItem(

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var orderProducts: MutableList<OrderProduct>,

    open var productCount: Int,

): BaseEntity()
