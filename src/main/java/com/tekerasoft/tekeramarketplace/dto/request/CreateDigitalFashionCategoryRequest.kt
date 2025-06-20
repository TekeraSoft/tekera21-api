package com.tekerasoft.tekeramarketplace.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

data class CreateDigitalFashionCategoryRequest (
    @field:NotBlank(message = "Category name cannot be blank")
    val name: String? = null,

    @field:NotNull(message = "File must be provided")
    val file: MultipartFile? = null
)