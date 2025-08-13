package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.SellerVerification

data class SellerVerificationSupportDto(
    val sellerUser: UserDto,
    val supervisor: UserDto,
    val seller: SellerAdminDto,
) {
    companion object {
        @JvmStatic
        fun toDto(from: SellerVerification): SellerVerificationSupportDto {
           return SellerVerificationSupportDto(
                from.sellerUser.let { it -> UserDto.toDto(it) },
                from.supervisor.let { it -> UserDto.toDto(it) },
                from.seller.let { it -> SellerAdminDto.toDto(it) }
            )
        }
    }
}