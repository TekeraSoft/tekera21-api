package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.ShippingCompany
import java.math.BigDecimal

data class ShippingCompanyDto(
    val id: String?,
    val name: String,
    val price: BigDecimal,
    val minDeliveryDay:Int,
    val maxDeliveryDay:Int,
) {
    companion object {
        @JvmStatic
        fun toDto(from: ShippingCompany): ShippingCompanyDto {
            return ShippingCompanyDto(
                from.id.toString(),
                from.name,
                from.price,
                from.minDeliveryDay,
                from.maxDeliveryDay
            )
        }
    }
}
