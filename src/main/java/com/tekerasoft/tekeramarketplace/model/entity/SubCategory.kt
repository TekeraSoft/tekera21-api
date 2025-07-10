package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
open class SubCategory(
    open var name: String,
    open var slug: String,
    open var image: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    open var category: Category,

    // 🌿 Alt kategori -> Üst kategori ilişkisi (Parent)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    open var parent: SubCategory? = null,

    // 🌱 Üst kategori -> Alt kategoriler ilişkisi (Children)
    @OneToMany(mappedBy = "parent", cascade = [CascadeType.ALL], orphanRemoval = true)
    open var children: MutableList<SubCategory> = mutableListOf()

) : BaseEntity()