package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name="news")
open class New(

    open var head: String,

    open var subTitle: String,

    open var image: String,

    @Column(columnDefinition = "TEXT")
    open var body: String,

    open var isActive: Boolean

): BaseEntity()
