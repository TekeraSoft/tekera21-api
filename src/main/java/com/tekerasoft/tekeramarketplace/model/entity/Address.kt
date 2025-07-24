package com.tekerasoft.tekeramarketplace.model.entity

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Embeddable
import jakarta.persistence.Entity

@Entity
open class Address (
    @JsonProperty("city")
    open var city: String,
    @JsonProperty("street")
    open var street: String,
    @JsonProperty("postalCode")
    open var postalCode: String? = null,
    @JsonProperty("buildNo")
    open var buildNo: String,
    @JsonProperty("doorNumber")
    open var doorNumber: String,
    @JsonProperty("detailAddress")
    open var detailAddress: String,
    @JsonProperty("country")
    open var country: String,
): BaseEntity()