package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Comment
import java.util.UUID

data class CommentDto(
    val id: UUID?,
    val userName: String,
    val rate: Double,
    val message: String
) {
    companion object{
        @JvmStatic
        fun toDto(from:Comment): CommentDto {
            return CommentDto(
                from.id,
                from.userName,
                from.rate,
                from.message
            )
        }
    }
}
