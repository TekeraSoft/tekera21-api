package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Address
import com.tekerasoft.tekeramarketplace.model.entity.BankAccount
import com.tekerasoft.tekeramarketplace.model.entity.Seller
import com.tekerasoft.tekeramarketplace.model.entity.SellerDocument
import com.tekerasoft.tekeramarketplace.model.entity.ShippingCompany
import com.tekerasoft.tekeramarketplace.model.enums.VerificationStatus
import java.time.LocalDateTime
import java.util.UUID

data class SellerAdminDto(
    val id: UUID?,
    val name: String,
    val slug: String,
    val categories: List<CompanyCategoryDto>,
    val logo: String,
    val email: String,
    val gsmNumber: String,
    val alternativePhoneNumber: String,
    val supportPhoneNumber: String,
    val taxNumber: String,
    val taxOffice: String,
    val merisNumber: String,
    val registrationDate: LocalDateTime,
    val contactPersonNumber: String,
    val contactPersonTitle: String,
    val address: List<Address>,
    val shippingCompanies: List<ShippingCompanyDto>,
    val bankAccounts: List<BankAccount>,
    val identityDocumentPaths: List<SellerDocument>,
    val rate: Double,
    val verificationStatus: VerificationStatus,
) {
    companion object {
        @JvmStatic
        fun toDto(from:Seller): SellerAdminDto {
            return SellerAdminDto(
                from.id,
                from.name,
                from.slug,
                from.categories.map { CompanyCategoryDto.toDto(it) },
                from.logo,
                from.email,
                from.gsmNumber,
                from.alternativePhoneNumber,
                from.supportPhoneNumber,
                from.taxNumber,
                from.taxOffice,
                from.merisNumber,
                from.registrationDate,
                from.contactPersonNumber,
                from.contactPersonTitle,
                from.address,
                from.shippingCompanies.map { ShippingCompanyDto.toDto(it) },
                from.bankAccounts,
                from.identityDocumentPaths,
                from.rate,
                from.verificationStatus,
            )
        }
    }
}
