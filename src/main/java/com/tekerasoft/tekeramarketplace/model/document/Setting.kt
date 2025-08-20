package com.tekerasoft.tekeramarketplace.model.document

import jakarta.persistence.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document(collection = "settings")
open class Setting(
    @Id
    open var id: String? = null,
    open var platformUsageFee: BigDecimal,
    open var minShippingPrice: BigDecimal,
    open var shippingPrice: BigDecimal,
    open var sellerSupportMaxAssignCount: Int
) {
    constructor(): this(
        "",
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        0
    )
}