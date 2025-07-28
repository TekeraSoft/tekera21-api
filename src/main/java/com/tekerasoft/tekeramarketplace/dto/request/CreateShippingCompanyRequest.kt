package com.tekerasoft.tekeramarketplace.dto.request

import java.math.BigDecimal

data class CreateShippingCompanyRequest(
    val name: String,
    val gsmNumber: String,
    val email: String,
    val price: BigDecimal,
    val minDeliveryDay: Int,
    val maxDeliveryDay: Int,
)
