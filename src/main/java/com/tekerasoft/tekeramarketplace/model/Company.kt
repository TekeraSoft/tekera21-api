package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.*
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "companies")
open class Company(
    open var name: String,
    open var category: String,
    open var merisNumber: String,
    open var logo: String,
    open var mail: String,
    open var gsmNumber: String,
    open var alternativePhoneNumber: String,
    open var supportPhoneNumber: String,

    open var taxNumber: String,
    open var taxOffice: String,
    open var registrationDate: LocalDateTime,

    open var contactPersonNumber: String,
    open var contactPersonTitle: String,

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    open var products: MutableList<Product>,

    @ElementCollection
    @CollectionTable(name = "company_address", joinColumns = [JoinColumn(name = "company_id")])
    open var address: MutableList<Address> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name = "company_bank_accounts", joinColumns = [JoinColumn(name = "company_id")])
    open var bankAccounts: MutableList<BankAccount> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name= "company_document_paths", joinColumns = [JoinColumn(name = "company_id")])
    open var identityDocumentPaths: List<String>,


    open var isVerified: Boolean = false,
    open var verificationStatus: VerificationStatus

    ): BaseEntity()
