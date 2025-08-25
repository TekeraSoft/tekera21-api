package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile

data class ProductCommentRequest(
    val productId: String,
    @JsonProperty("images")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val images: MutableList<MultipartFile>? = mutableListOf(),
    val message: String,
)
