package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile

data class UpdateFashionCollectionRequest(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("collectionName")
    val collectionName: String,
    @JsonProperty("products")
    val products: List<String>,
    @JsonProperty("image")
    val image: MultipartFile? = null,
    @JsonProperty("description")
    val description: String? = null
)
