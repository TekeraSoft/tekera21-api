package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.CascadeType
import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToMany
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
open class Comment(

    open var rate: Double,
    @Column(columnDefinition = "TEXT")
    open var message: String,

    open var likeCount: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    open var user: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    open var product: Product? = null,

    @ElementCollection
    @CollectionTable(name= "product_comment_images", joinColumns = [JoinColumn(name = "comment_fk")])
    open var productImages: MutableList<String> = mutableListOf(),

): BaseEntity() {
    constructor() : this(0.0,"",0, User(), null)
}
