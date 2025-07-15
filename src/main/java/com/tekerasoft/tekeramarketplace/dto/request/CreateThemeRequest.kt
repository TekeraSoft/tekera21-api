package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import org.springframework.web.multipart.MultipartFile

data class CreateThemeRequest(

    @field:NotBlank(message = "Theme name cannot be blank")
    @JsonProperty("name")
    val name: String,

    @JsonProperty("image")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val image: MultipartFile? = null,

)