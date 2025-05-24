package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "companies")
open class Company(
    open var name: String,
    open var category: String,
    open var logo: String,
    open var email: String,
    open var gsmNumber: String,
    open var alternativePhoneNumber: String,
    open var supportPhoneNumber: String,

    open var taxNumber: String,
    open var taxOffice: String,
    open var merisNumber: String,
    open var registrationDate: LocalDateTime,

    open var contactPersonNumber: String,
    open var contactPersonTitle: String,

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    open var products: MutableList<Product> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name = "company_address", joinColumns = [JoinColumn(name = "company_id")])
    open var address: MutableList<Address> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name = "company_bank_accounts", joinColumns = [JoinColumn(name = "company_id")])
    open var bankAccounts: MutableList<BankAccount> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name= "company_document_paths", joinColumns = [JoinColumn(name = "company_id")])
    open var identityDocumentPaths: List<CompanyDocument>,

    open var isVerified: Boolean = false,

    @Enumerated(EnumType.STRING)
    open var verificationStatus: VerificationStatus = VerificationStatus.PENDING

    ): BaseEntity()
