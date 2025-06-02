package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.TargetPicture
import java.time.LocalDateTime
import java.util.UUID

data class TargetPictureDto(
    val id: UUID?,
    val targetPic: String,
    val mindPath: String?,
    val createdAt: LocalDateTime?,
) {
    companion object {
        @JvmStatic
        fun toDto(from: TargetPicture): TargetPictureDto {
            return TargetPictureDto(
                from.id,
                from.targetPic,
                from.mindPath,
                from.createdAt
            )
        }
    }
}