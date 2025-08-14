package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import org.springframework.web.multipart.MultipartFile

data class CreateTargetPictureRequest(
    @JsonProperty("productId")
    @NotNull
    @NotBlank
    val productId: String,
    @JsonProperty("image")
    val image: MultipartFile,
    @JsonProperty("defaultContent")
    val defaultContent: MultipartFile,
    @JsonProperty("specialContent")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val specialContent: MultipartFile? = null,
)