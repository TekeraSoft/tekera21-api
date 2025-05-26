package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*

@Entity
open class Order(

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var basketItems: MutableList<BasketItem>,

    @ManyToOne(fetch = FetchType.LAZY)
    open var user: User

): BaseEntity()
