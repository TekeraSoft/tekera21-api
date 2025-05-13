package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.*
import java.util.UUID

@Entity
open class Company(
    open var name: String,
    open var category: String,
    open var merisNumber: String,
    open var logo: String,
    open var mail: String,
    open var gsmNumber: String,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var address: Address,

//    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
//    open var companyRepresentative: User,

    @ElementCollection
    @CollectionTable(name= "document_paths", joinColumns = [JoinColumn(name = "company_id")])
    open var documentPaths: List<String>

    ): BaseEntity()
