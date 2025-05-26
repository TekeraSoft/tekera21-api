package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity

@Entity
open class Comment(

    open var userName: String,
    open var rate: Double,
    @Column(columnDefinition = "TEXT")
    open var message: String,

): BaseEntity()
