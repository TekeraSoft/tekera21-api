package com.tekerasoft.tekeramarketplace.dto.document

import com.tekerasoft.tekeramarketplace.model.document.TargetPicture
import java.time.LocalDateTime

data class TargetPictureDto(
    val id: String?,
    val targetPic: String,
    val mindPath: String,
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