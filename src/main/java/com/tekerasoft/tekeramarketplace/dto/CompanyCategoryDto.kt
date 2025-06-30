package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Category
import java.util.UUID

data class CompanyCategoryDto(
    val id: UUID?,
    val name: String,
    val image: String?
) {
    companion object {
        @JvmStatic
        fun toDto(from: Category): CompanyCategoryDto {
            return CompanyCategoryDto(
                from.id,
                from.name,
                from.image
            )
        }
    }
}
