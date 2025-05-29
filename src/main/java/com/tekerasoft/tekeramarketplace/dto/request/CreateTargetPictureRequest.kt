package com.tekerasoft.tekeramarketplace.dto.request

import org.springframework.web.multipart.MultipartFile

data class CreateTargetPictureRequest(
    val image: MultipartFile,
)
