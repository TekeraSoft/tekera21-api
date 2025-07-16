package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Address

data class AddressDto(
    val city: String,
    val street: String,
    val postalCode: String,
    val buildNo: String,
    val doorNumber: String,
    val detailAddress: String,
    val country: String
) {
    companion object {
        @JvmStatic
        fun toDto(from: Address): AddressDto {
            return AddressDto(
                from.city,
                from.street,
                from.postalCode,
                from.buildNo,
                from.doorNumber,
                from.detailAddress,
                from.country
            )
        }
    }
}
