package com.tekerasoft.tekeramarketplace.dto

import java.math.BigDecimal

data class SellerReportAggregation(
    val dailyProfit: BigDecimal,
    val weeklyProfit: BigDecimal,
    val monthlyProfit: BigDecimal
)
