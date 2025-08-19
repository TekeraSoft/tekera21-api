package com.tekerasoft.tekeramarketplace.model.document

import org.springframework.data.mongodb.core.mapping.Document
import java.math.BigDecimal

@Document(collection = "settings")
open class Setting(
    open var platformUsageFee: BigDecimal,
    open var minShippingPrice: BigDecimal,
    open var shippingPrice: BigDecimal,
) {
    constructor(): this(
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO
    )
}