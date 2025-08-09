package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.VerificationStatus
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "sellers")
open class Seller(
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
        name = "seller_categories",
        joinColumns = [JoinColumn(name = "seller_id")],
        inverseJoinColumns = [JoinColumn(name = "category_id")])
    open var categories: MutableSet<Category> = mutableSetOf(),

    @OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var products: MutableList<Product> = mutableListOf(),

    @OneToMany(fetch = FetchType.LAZY)
    open var users: MutableSet<User> = mutableSetOf(),

    @OneToMany(cascade = [(CascadeType.ALL)], fetch = FetchType.LAZY)
    open var address: MutableList<Address> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name = "seller_bank_accounts",
        joinColumns = [JoinColumn(name = "seller_id")])
    open var bankAccounts: MutableList<BankAccount> = mutableListOf(),

    @ElementCollection
    @CollectionTable(name= "seller_document_paths",
        joinColumns = [JoinColumn(name = "seller_id")])
    open var identityDocumentPaths: MutableList<SellerDocument> = mutableListOf(),

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "seller_shipping_companies",
        joinColumns = [JoinColumn(name = "seller_id")],
        inverseJoinColumns = [JoinColumn(name = "shipping_company_id")]
    )
    open var shippingCompanies: MutableSet<ShippingCompany> = mutableSetOf(),

    @OneToMany(fetch = FetchType.LAZY)
    open var sellerOrders: MutableList<SellerOrder> = mutableListOf(),

    open var rate: Double,

    open var isVerified: Boolean = false,

    open var isActive: Boolean = false,

    @Enumerated(EnumType.STRING)
    open var verificationStatus: VerificationStatus = VerificationStatus.PENDING

    ): BaseEntity() {

        constructor() : this(
            name = "",
            slug = "",
            logo = "",
            email = "",
            gsmNumber = "",
            estimatedDeliveryTime = "",
            alternativePhoneNumber = "",
            supportPhoneNumber = "",
            taxNumber = "",
            taxOffice = "",
            merisNumber = "",
            registrationDate = LocalDateTime.now(),
            contactPersonNumber = "",
            contactPersonTitle = "",
            categories = mutableSetOf(),
            products = mutableListOf(),
            users = mutableSetOf(),
            address = mutableListOf(),
            bankAccounts = mutableListOf(),
            identityDocumentPaths = mutableListOf(),
            shippingCompanies = mutableSetOf(),
            sellerOrders = mutableListOf(),
            rate = 0.0,
            isVerified = false,
            isActive = false,
            verificationStatus = VerificationStatus.PENDING,
        )
    }
