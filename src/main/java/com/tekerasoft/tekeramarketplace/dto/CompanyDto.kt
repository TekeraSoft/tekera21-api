package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Address
import com.tekerasoft.tekeramarketplace.model.entity.BankAccount
import com.tekerasoft.tekeramarketplace.model.entity.Company
import com.tekerasoft.tekeramarketplace.model.entity.CompanyDocument
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import java.time.LocalDateTime

data class CompanyDto(

    val name: String,
    val category: String,
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

    val products: Page<ProductDto>,
    val address: List<Address>,
    val bankAccounts: List<BankAccount>,

    val identityDocumentPaths: List<CompanyDocument>,

    val rate: Double,

) {
    companion object {
        @JvmStatic
        fun toDto(from:Company): CompanyDto {
            return CompanyDto(
                from.name,
                from.category,
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
                PageImpl(from.products.map {ProductDto.toDto(it) }),
                from.address,
                from.bankAccounts,
                from.identityDocumentPaths,
                from.rate
            )
        }
    }
}
