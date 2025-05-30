package com.tekerasoft.tekeramarketplace.dto.payload

import org.springframework.web.multipart.MultipartFile

data class TargetPicturePayload(
    val image: MultipartFile
)
