package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Embeddable

@Embeddable
data class AttributeDetail(
    val key: String = "",
    val value: String = "",
) {
    constructor() : this("","")
}
