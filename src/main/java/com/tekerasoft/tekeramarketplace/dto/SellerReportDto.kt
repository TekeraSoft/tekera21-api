package com.tekerasoft.tekeramarketplace.dto

import org.springframework.data.domain.Page

data class SellerReportDto(
    val sellerReportAggregation: SellerReportAggregation,
    val followers: List<SellerFollowerDto>,
    val totalOrders: Long,
    val totalProducts: Long,
    val recentOrders: Page<SellerRecentOrderDto>,
    val topProducts: Page<ProductUiDto>
)