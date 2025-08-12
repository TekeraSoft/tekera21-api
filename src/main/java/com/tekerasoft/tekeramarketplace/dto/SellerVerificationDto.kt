package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Seller
import com.tekerasoft.tekeramarketplace.model.enums.ExtraDocument
import com.tekerasoft.tekeramarketplace.model.enums.SellerDocumentType

data class SellerVerificationDto(
    val sellerUser: UserDto,
    val seller: Seller,
    val checkDocumentVerification: Set<SellerDocumentType>,
    val extraDocumentVerification: Set<ExtraDocument>,
    val checkSignature: Boolean
)
