package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Embeddable

@Embeddable
open class BasketAttributes(
    open var key: String,
    open var value: String,
)
