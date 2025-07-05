package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile

data class UpdateThemeRequest(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val name: String? = null,
    @JsonProperty("image")
    val image: MultipartFile? = null,
)
