package com.tekerasoft.tekeramarketplace.dto

import java.math.BigDecimal

data class SellerInterruptionDto(
    val calculateDate: CalculateDate,
    val interruptionContent: List<InterruptionContent>,
    val sellerFee: BigDecimal,
    val interruptionAmount: BigDecimal,
)

data class InterruptionContent(
    val orderNumber: String,
    val productName: String,
    val modelCode: String,
    val productImageUrl: String,
    val platformUsageFee: Interruption,
    val platformCommission: Interruption,
    val sellerProfit: BigDecimal
)

data class Interruption (
    val description: String,
    val value: BigDecimal,
)

data class CalculateDate(
    val month: String,
    val year: String,
)