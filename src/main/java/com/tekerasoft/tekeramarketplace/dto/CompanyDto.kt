package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Address
import com.tekerasoft.tekeramarketplace.model.entity.BankAccount
import com.tekerasoft.tekeramarketplace.model.entity.Company
import com.tekerasoft.tekeramarketplace.model.entity.CompanyDocument
import java.time.LocalDateTime
import java.util.UUID

data class CompanyDto(
    val id: UUID?,
    val name: String,
    val categories: List<CategoryDto>,
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
//    val products: Page<ProductDto>,
    val address: List<Address>,
    val bankAccounts: List<BankAccount>,
    val identityDocumentPaths: List<CompanyDocument>,
    val rate: Double,
) {
    companion object {
        @JvmStatic
        fun toDto(from:Company): CompanyDto {
            return CompanyDto(
                from.id,
                from.name,
                from.categories.map { CategoryDto.toDto(it) },
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
//                PageImpl(from.products.map {ProductDto.toDto(it) }),
                from.address,
                from.bankAccounts,
                from.identityDocumentPaths,
                from.rate
            )
        }
    }
}
