package com.tekerasoft.tekeramarketplace.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Embeddable

@Embeddable
data class BankAccount(
    @JsonProperty("iban")
    var iban: String,
    @JsonProperty("accountName")
    var accountName: String,
    @JsonProperty("bankName")
    var bankName: String,
    @JsonProperty("isActive")
    var isActive: Boolean,
) {
    constructor() : this("","","",false)
}