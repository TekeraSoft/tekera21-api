package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Address
import com.tekerasoft.tekeramarketplace.model.entity.BankAccount
import com.tekerasoft.tekeramarketplace.model.entity.CompanyDocument
import org.springframework.data.domain.Page
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
    val address: Page<Address>,
    val bankAccounts: List<BankAccount>,

    val identityDocumentPaths: List<CompanyDocument>,

    val rate: Double,

)
