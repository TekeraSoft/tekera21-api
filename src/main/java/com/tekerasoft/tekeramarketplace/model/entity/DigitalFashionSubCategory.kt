package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
open class DigitalFashionSubCategory(
    open var name: String,
    open var image: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "digital_fashion_category_id")
    open var digitalFashionCategory: DigitalFashionCategory,
): BaseEntity()