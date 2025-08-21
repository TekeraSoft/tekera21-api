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
    open var sellerSupportMaxAssignCount: Int,
    open var platformCommissionFee: BigDecimal? = null,
    open var taxPercents: MutableList<TaxPercent>? = mutableListOf(),
) {
    constructor(): this(
        "",
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        BigDecimal.ZERO,
        0,
        BigDecimal.ZERO,
        mutableListOf(),
    )
}

open class TaxPercent(
    open var key: String,
    open var value: BigDecimal,
)