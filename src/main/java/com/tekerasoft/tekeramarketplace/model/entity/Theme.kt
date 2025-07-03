package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity

@Entity
open class Theme(
    val name: String,
    val image: String,
    val isActive: Boolean
): BaseEntity()
