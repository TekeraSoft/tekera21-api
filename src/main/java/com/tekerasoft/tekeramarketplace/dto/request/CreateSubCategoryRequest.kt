package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

data class CreateSubCategoryRequest(

    @field:NotBlank(message = "Sub category name cannot be blank")
    @field:NotNull
    @JsonProperty("name")
    val name: String,

    @field:NotNull(message = "File must be provided")
    @JsonProperty("image")
    val image: MultipartFile,

    @field:NotBlank(message = "Category id must be provided")
    @field:NotNull
    @JsonProperty("categoryId")
    val categoryId: String
)