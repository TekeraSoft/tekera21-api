package com.tekerasoft.tekeramarketplace.dto

import java.math.BigDecimal

data class SellerReportDto(
    val sellerReportAggregation: SellerReportAggregation,
    val interruptions: List<Interruption>
)

data class Interruption (
    val shippingPrice: BigDecimal,
    val platformUsageFee: BigDecimal,

)