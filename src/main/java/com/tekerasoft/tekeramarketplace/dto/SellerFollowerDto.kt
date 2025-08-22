package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.User

data class SellerFollowerDto(
    val firstName: String,
    val lastName: String,
) {
    companion object {
        @JvmStatic
        fun toDto(user: User): SellerFollowerDto {
            return SellerFollowerDto(
                user.firstName,
                user.lastName,
            )
        }
    }
}
