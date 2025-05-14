package com.tekerasoft.tekeramarketplace.model

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.persistence.Embeddable

@Embeddable
data class Attribute(
    @JsonProperty("key")
    var key: String = "",
    @JsonProperty("value")
    var value: String = "",
)
