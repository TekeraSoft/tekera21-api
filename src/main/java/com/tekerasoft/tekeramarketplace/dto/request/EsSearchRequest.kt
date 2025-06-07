package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank

data class EsSearchRequest(
    @JsonProperty("fieldName")
    @NotBlank(message = "FieldName cannot be blank")
    val fieldName: List<String>,

    @JsonProperty("searchValue")
    @NotBlank(message = "SearchValue cannot be blank")
    val searchValue: List<String>
)
