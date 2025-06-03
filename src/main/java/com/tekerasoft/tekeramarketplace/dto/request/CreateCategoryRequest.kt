package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

data class CreateCategoryRequest(
    @field:NotBlank(message = "Category name cannot be blank")
    @JsonProperty("name")
    val name: String? = null,

    @field:NotNull(message = "File must be provided")
    @JsonProperty("file")
    val file: MultipartFile? = null
)
