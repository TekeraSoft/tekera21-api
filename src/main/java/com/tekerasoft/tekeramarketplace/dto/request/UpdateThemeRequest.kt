package com.tekerasoft.tekeramarketplace.dto.request

import org.springframework.web.multipart.MultipartFile
import java.util.UUID

data class UpdateThemeRequest(
    val id: String,
    val name: String,
    val image: MultipartFile,
    val deleteImages: List<String>
)
