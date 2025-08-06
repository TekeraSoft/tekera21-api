package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.SellerVerification
import com.tekerasoft.tekeramarketplace.model.enums.ExtraDocument

data class SellerVerificationSupportDto(
    val sellerUser: UserDto,
    val seller: SellerAdminDto,
    val sellerDocuments: Set<com.tekerasoft.tekeramarketplace.model.enums.SellerDocument>,
    val sellerExtraDocument: Set<ExtraDocument>,
    val eSignature: Boolean
) {
    companion object {
        @JvmStatic
        fun toDto(from: SellerVerification): SellerVerificationSupportDto {
           return SellerVerificationSupportDto(
                from.sellerUser.let { it -> UserDto.toDto(it) },
                from.seller.let { it -> SellerAdminDto.toDto(it) },
                from.seller.identityDocumentPaths
                    .mapNotNull { it.documentTitle } // null'ları atar
                    .toSet(),
                from.extraDocumentVerification.toMutableSet(),
                from.checkESignature
            )
        }
    }
}
