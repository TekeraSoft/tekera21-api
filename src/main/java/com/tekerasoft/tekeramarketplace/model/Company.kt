package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.UUID

@Entity
open class Company(
    open var name: String,
    open var address: String,
    open var city: String,
): BaseEntity()
