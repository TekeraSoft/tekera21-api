package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile

data class CreateCategoryRequest(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("file")
    val file: MultipartFile
)
