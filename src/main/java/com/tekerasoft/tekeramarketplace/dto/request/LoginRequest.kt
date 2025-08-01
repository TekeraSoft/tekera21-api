package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty


data class LoginRequest(
    @JsonProperty("email")
    val email: String,
    @JsonProperty("password")
    val password: String,
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val cartId: String? = null,
)
