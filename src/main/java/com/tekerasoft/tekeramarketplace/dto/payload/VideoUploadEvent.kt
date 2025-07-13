package com.tekerasoft.tekeramarketplace.dto.payload

import java.util.UUID

data class VideoUploadEvent(
    val productId: UUID,
    val tempFilePath: String,
    val originalFilename: String,
    val contentType: String,
    val size: Long,
    val companySlug: String,
)
