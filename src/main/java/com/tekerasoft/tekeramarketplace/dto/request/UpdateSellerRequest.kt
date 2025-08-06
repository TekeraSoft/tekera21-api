package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.Address
import com.tekerasoft.tekeramarketplace.model.entity.BankAccount
import java.time.LocalDateTime

data class UpdateSellerRequest(

    @JsonProperty("id")
    val id: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("categoryId")
    val categoryId: List<String>,

    @JsonProperty("email")
    val email: String,

    @JsonProperty("gsmNumber")
    val gsmNumber: String,

    @JsonProperty("alternativePhoneNumber")
    val alternativePhoneNumber: String,

    @JsonProperty("supportPhoneNumber")
    val supportPhoneNumber: String,

    @JsonProperty("taxNumber")
    val taxNumber: String,

    @JsonProperty("taxOffice")
    val taxOffice: String,

    @JsonProperty("merisNumber")
    val merisNumber: String,

    @JsonProperty("registrationDate") // JSON da buna uygun olmalı
    val registrationDate: LocalDateTime,

    @JsonProperty("contactPersonNumber")
    val contactPersonNumber: String,

    @JsonProperty("contactPersonTitle")
    val contactPersonTitle: String,

    @JsonProperty("address")
    val address: List<Address>,

    @JsonProperty("bankAccount") // JSON'daki isme uygun hale getirildi
    val bankAccount: List<BankAccount>,

    @JsonProperty("shippingCompanies")
    val shippingCompanies: List<String>
)
