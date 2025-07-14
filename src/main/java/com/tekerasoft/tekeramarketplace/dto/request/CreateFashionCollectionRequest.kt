package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.web.multipart.MultipartFile

open class CreateFashionCollectionRequest(
    @JsonProperty("collectionName")
    val collectionName: String,
    @JsonProperty("products")
    val products: List<String>,
    @JsonProperty("image")
    val image: MultipartFile? = null,
    @JsonProperty("description")
    val description: String? = null,
    @JsonProperty("companyId")
    val companyId: String,
)
