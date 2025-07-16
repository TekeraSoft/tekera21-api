package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "companies")
open class Company(
    open var name: String,
    open var slug: String,
    open var logo: String,
    open var email: String,
    open var gsmNumber: String,
    open var estimatedDeliveryTime: String,
    open var alternativePhoneNumber: String,
    open var supportPhoneNumber: String,

    open var taxNumber: String,
    open var taxOffice: String,
    open var merisNumber: String,
    open var registrationDate: LocalDateTime,

    open var contactPersonNumber: String,
    open var contactPersonTitle: String,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "company_categories",
        joinColumns = [JoinColumn(name = "company_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")])
    open var categories: MutableSet<Category> = mutableSetOf(),

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    open var products: MutableList<Product> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY, cascade = [(CascadeType.ALL)])
    open var users: MutableSet<User> = mutableSetOf(),

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    open var address: MutableList<Address> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name = "company_bank_accounts",
        joinColumns = [JoinColumn(name = "company_id")])
    open var bankAccounts: MutableList<BankAccount> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name= "company_document_paths",
        joinColumns = [JoinColumn(name = "company_id")])
    open var identityDocumentPaths: List<CompanyDocument>,

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "company_shipping_companies",
        joinColumns = [JoinColumn(name = "company_id")],
        inverseJoinColumns = [JoinColumn(name = "shipping_company_id")]
    )
    open var shippingCompanies: MutableSet<ShippingCompany> = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY)
    open var orders: MutableList<Order> = mutableListOf(),

    open var rate: Double,

    open var isVerified: Boolean = false,

    open var isActive: Boolean = true,

    @Enumerated(EnumType.STRING)
    open var verificationStatus: VerificationStatus = VerificationStatus.PENDING

    ): BaseEntity()
