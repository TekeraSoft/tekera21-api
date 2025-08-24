package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.config.NoArg
import com.tekerasoft.tekeramarketplace.model.enums.CurrencyType
import com.tekerasoft.tekeramarketplace.model.enums.ProductType
import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OrderColumn
import jakarta.persistence.Table

@Entity
@Table(name = "products")
@NoArg
open class Product(

    open var name: String,
    open var slug: String,
    open var code: String? = null,
    open var brandName: String? = null,

    @Column(columnDefinition = "TEXT")
    open var description: String? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    open var category: Category? = null,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_subcategories", joinColumns = [JoinColumn(name = "product_fk")],
        inverseJoinColumns = [JoinColumn(name = "subcategory_id")])
    open var subCategories: MutableSet<SubCategory>? = mutableSetOf(),

    @OneToMany(mappedBy="product",fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @OrderColumn(name = "position")
    open var variations: MutableList<Variation> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    open var currencyType: CurrencyType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    open var seller: Seller,

    open var shippingPreparationDays: Int? = 0,

    @ElementCollection
    @CollectionTable(name= "product_tags", joinColumns = [JoinColumn(name = "product_fk")])
    open var tags: MutableList<String>? = mutableListOf(),

    @Enumerated(EnumType.STRING)
    open var productType: ProductType,

    @ElementCollection
    @CollectionTable(name = "product_attributes", joinColumns = [JoinColumn(name = "product_id")])
    open var attributes: MutableList<AttributeDetail> = mutableListOf(),

    open var rate: Double = 0.0,

    open var likeCount: Int = 0,

    open var videoUrl: String? = null,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var comments: MutableList<Comment> = mutableListOf(),

    @Column(nullable = false)
    open var isActive: Boolean = true

    ): BaseEntity() {
        constructor(): this(
            name = "",
            slug = "",
            code = null,
            brandName = null,
            description = null,
            category = null,
            subCategories = null,
            variations = mutableListOf(),
            currencyType = CurrencyType.TRY,
            seller = Seller(),
            shippingPreparationDays = 0,
            tags = mutableListOf(),
            productType = ProductType.PHYSICAL,
            rate = 0.0,
            videoUrl = null,
            comments = mutableListOf(),
            isActive = false
        )
    }
