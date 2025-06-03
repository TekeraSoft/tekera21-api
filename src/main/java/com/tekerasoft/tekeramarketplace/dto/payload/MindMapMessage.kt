package com.tekerasoft.tekeramarketplace.dto.payload

import java.util.UUID

data class MindMapMessage(
    val id: UUID,
    val filePath: String
)