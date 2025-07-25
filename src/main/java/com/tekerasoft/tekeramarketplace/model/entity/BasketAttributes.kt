package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity

@Entity
open class BasketAttributes(
    open var key: String,
    open var value: String,
): BaseEntity()
