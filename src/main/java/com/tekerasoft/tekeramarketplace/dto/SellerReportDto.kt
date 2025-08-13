package com.tekerasoft.tekeramarketplace.dto

import java.math.BigDecimal

data class SellerReportDto(
    val dailyProfit: BigDecimal,
    val weeklyProfit: BigDecimal,
    val monthlyProfit: BigDecimal,
    val specificDateProfit: BigDecimal,
    val calculateShippingPriceSpecificDate: BigDecimal,
    val interruptions: List<Interruption>
)

data class Interruption (
    val shippingPrice: BigDecimal,
    val platformUsageFee: BigDecimal,

)
