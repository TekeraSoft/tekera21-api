package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany

@Entity
open class FashionCollection(

    open var collectionName: String,
    open var slug: String,
    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "fashion_collection_products",                // ara tablo
        joinColumns = [JoinColumn(name = "fashion_collection_id")],   // bu entity’nin FK’si
        inverseJoinColumns = [JoinColumn(name = "product_id")]        // karşı tarafın FK’si
    )
    open var products: MutableList<Product> = mutableListOf(),

    open var image: String? = null,

    @Column(columnDefinition = "TEXT")
    open var description: String? = null,

    open var isActive: Boolean = true,

): BaseEntity()