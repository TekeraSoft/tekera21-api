package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.Entity

@Entity
open class Address (
    open var city: String,
    open var street: String,
    open var postalCode: String,
    open var buildNo: String,
    open var doorNumber: String,
    open var detailAddress: String,
    open var country: String,
): BaseEntity()