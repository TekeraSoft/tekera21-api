package com.tekerasoft.tekeramarketplace.dto.request

import org.springframework.web.multipart.MultipartFile

data class CreateDigitalFashionSubCategoryRequest(
    val name: String,
    val image: MultipartFile
)
