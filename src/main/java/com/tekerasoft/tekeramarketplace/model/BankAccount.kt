package com.tekerasoft.tekeramarketplace.model

import jakarta.persistence.Embeddable

@Embeddable
open class BankAccount(
    open var iban: String,
    open var accountName: String,
    open var bankName: String,
    open var isActive: Boolean,
)
