package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "themes")
open class Theme(
    open var name: String,
    open var image: String,
): BaseEntity()
