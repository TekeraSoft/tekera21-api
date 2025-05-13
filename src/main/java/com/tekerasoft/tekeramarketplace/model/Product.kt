package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

@Entity
open class Product(

    open var name: String,
    open var slug: String,
    open var code: String,
    open var brandName: String,

    @Column(columnDefinition = "TEXT")
    open var description: String,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    open var category: Category,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "product_subcategories",
        joinColumns = [JoinColumn(name = "product_id")],
        inverseJoinColumns = [JoinColumn(name = "subcategory_id")])
    open var subCategories: MutableSet<SubCategory> = mutableSetOf(),

    @OneToMany(mappedBy="product",fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var variations: MutableList<Variation> = mutableListOf(),

    @Enumerated(EnumType.STRING)
    open var currencyType: CurrencyType,

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "company_id")
//    open var company: Company,

    @ElementCollection
    @CollectionTable(name= "product_tags", joinColumns = [JoinColumn(name = "product_id")])
    open var tags: List<String>,

    @Enumerated(EnumType.STRING)
    open var productType: ProductType,

    @ElementCollection
    @CollectionTable(name = "product_attributes", joinColumns = [JoinColumn(name = "product_id")])
    open var attributes: List<Attribute> = listOf()

): BaseEntity()
