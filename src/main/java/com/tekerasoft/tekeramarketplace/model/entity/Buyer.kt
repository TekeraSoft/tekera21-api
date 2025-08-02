package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity

@Entity
open class Buyer(
    open var name: String,
    open var surname: String,
    open var email: String,
    open var gsmNumber: String,
    open var identityNumber: String,
    open var isRegistered: Boolean? = false,

): BaseEntity() {
    constructor() : this(
        name = "",
        surname = "",
        email = "",
        gsmNumber = "",
        identityNumber = "",
        isRegistered = false
    )
}