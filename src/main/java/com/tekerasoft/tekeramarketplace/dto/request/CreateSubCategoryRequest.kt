package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile

data class CreateSubCategoryRequest(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("image")
    val image: MultipartFile,
    @JsonProperty("categoryId")
    val categoryId: String
)