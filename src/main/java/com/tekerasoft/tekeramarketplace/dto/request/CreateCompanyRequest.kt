package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.Address
import com.tekerasoft.tekeramarketplace.model.BankAccount
import java.time.LocalDateTime

data class CreateCompanyRequest(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("category")
    val categoryName: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("gsmNumber")
    val gsmNumber: String,
    @JsonProperty("alternativePhoneNumber")
    val alternativePhoneNumber: String,
    @JsonProperty("name")
    val supportPhoneNumber: String,

    @JsonProperty("taxNumber")
    val taxNumber: String,
    @JsonProperty("taxOffice")
    val taxOffice: String,
    @JsonProperty("merisNumber")
    val merisNumber: String,
    @JsonProperty("registrationDate")
    val registrationDate: LocalDateTime,

    @JsonProperty("contactPersonNumber")
    val contactPersonNumber: String,
    @JsonProperty("contactPersonTitle")
    val contactPersonTitle: String,

    @JsonProperty("address")
    val address: List<Address>,

    @JsonProperty("bankAccounts")
    val bankAccount: List<BankAccount>,

)
