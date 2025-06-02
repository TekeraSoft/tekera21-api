package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.OneToMany

@Entity
open class DigitalFashionCategory(
    open var name: String,
    open var image: String,

    @OneToMany(mappedBy = "digitalFashionCategory", cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    open var digitalFashionSubCategory: MutableList<DigitalFashionSubCategory> = mutableListOf(),

): BaseEntity()
