package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Comment
import java.time.LocalDateTime

data class CommentUiDto(
    val commentId: String?,
    val firstName: String,
    val lastName: String,
    val rate: Double,
    val likeCount: Int,
    val message: String,
    val images: List<String>,
    val createdAt: LocalDateTime,
) {
    companion object{
        @JvmStatic
        fun toDto(from:Comment): CommentUiDto {
            return CommentUiDto(
                from.id.toString(),
                from.user.firstName,
                from.user.lastName,
                from.rate,
                from.likeCount,
                from.message,
                from.productImages,
                from.createdAt,
            )
        }
    }
}
