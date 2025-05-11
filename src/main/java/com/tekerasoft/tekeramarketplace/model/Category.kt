package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import java.util.UUID

@Entity
open class Category(
    open var name: String,
    open var image: String,

    @OneToMany(mappedBy = "category",cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    open var subCategories: MutableList<SubCategory> = mutableListOf(),

): BaseEntity()
