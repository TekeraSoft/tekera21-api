package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class SubCategory(
    open var name: String,
    open var image: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    open var category: Category,
): BaseEntity()
