package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.Embeddable

@Embeddable
data class Attribute(
    var name: String = "",
    var value: String = "",
)
