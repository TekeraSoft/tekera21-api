package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.Entity
import java.util.UUID

@Entity
data class Address(
    var id: UUID? = null,
)
