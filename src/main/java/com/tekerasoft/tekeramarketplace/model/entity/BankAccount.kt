package com.tekerasoft.tekeramarketplace.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Embeddable

@Embeddable
open class BankAccount(
    @JsonProperty("iban")
    open var iban: String,
    @JsonProperty("accountName")
    open var accountName: String,
    @JsonProperty("bankName")
    open var bankName: String,
    @JsonProperty("isActive")
    open var isActive: Boolean,
)