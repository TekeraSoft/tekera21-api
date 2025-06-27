package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.ManyToMany
import jakarta.persistence.OneToMany

@Entity
open class Category(
    open var name: String,
    open var slug: String,
    open var image: String,

    @OneToMany(mappedBy = "category",cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    open var subCategories: MutableList<SubCategory> = mutableListOf(),

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    open var companies: MutableList<Company> = mutableListOf()

    ): BaseEntity()
