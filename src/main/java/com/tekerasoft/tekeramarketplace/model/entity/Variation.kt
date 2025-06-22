package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "variations")
open class Variation(
    open var modelName: String,
    open var modelCode: String,
    open var color: String,

    @ElementCollection
    @CollectionTable(
        name = "variation_images",
        joinColumns = [JoinColumn(name = "variation_id")]
    )
    open var images: List<String> = listOf(),

    @OneToMany(mappedBy="variation",fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var attributes: MutableList<Attribute> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    open var product: Product,

    ): BaseEntity()