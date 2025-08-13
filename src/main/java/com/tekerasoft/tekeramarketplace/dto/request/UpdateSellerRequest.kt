package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.Address
import com.tekerasoft.tekeramarketplace.model.entity.BankAccount
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDateTime

data class UpdateSellerRequest(

    @JsonProperty("id")
    @field:NotBlank(message = "Seller id is required")
    val id: String,

    @JsonProperty("name")
    @field:NotBlank(message = "Name is required")
    val name: String,

    @JsonProperty("categoryId")
    @field:NotNull(message = "Category id is required")
    @field:Size(min = 1)
    val categoryId: List<String>,

    @JsonProperty("email")
    @field:NotBlank(message = "Email is required")
    val email: String,

    @JsonProperty("gsmNumber")
    @field:NotBlank(message = "GSM number is required")
    val gsmNumber: String,

    @JsonProperty("alternativePhoneNumber")
    @field:NotBlank(message = "Alternative phone number is required")
    val alternativePhoneNumber: String,

    @JsonProperty("supportPhoneNumber")
    @field:NotBlank(message = "Support phone number is required")
    val supportPhoneNumber: String,

    @JsonProperty("taxNumber")
    @field:NotBlank(message = "Tax number is required")
    val taxNumber: String,

    @JsonProperty("taxOffice")
    @field:NotBlank(message = "Tax office is required")
    val taxOffice: String,

    @JsonProperty("merisNumber")
    @field:NotBlank(message = "Meris number is required")
    val merisNumber: String,

    @JsonProperty("registrationDate") // JSON da buna uygun olmalı
    val registrationDate: LocalDateTime,

    @JsonProperty("contactPersonNumber")
    @field:NotBlank(message = "Contact person number is required")
    val contactPersonNumber: String,

    @JsonProperty("contactPersonTitle")
    @field:NotBlank(message = "Contact person title is required")
    val contactPersonTitle: String,

    @JsonProperty("address")
    @field:NotNull(message = "Contact address is required")
    @field:Size(min = 1)
    val address: List<Address>,

    @JsonProperty("bankAccount") // JSON'daki isme uygun hale getirildi
    val bankAccount: List<BankAccount>,

    @JsonProperty("shippingCompanies")
    val shippingCompanies: List<String>
)
