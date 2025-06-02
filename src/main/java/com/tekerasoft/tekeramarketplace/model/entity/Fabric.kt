package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name="fabrics")
open class Fabric(
    open var fabricName: String,
    open var fabricImage: String,
    open var fabricPrice: BigDecimal,
    open var stock: Int,
    open var color: String
): BaseEntity()
