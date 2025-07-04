package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Theme
import java.util.UUID

data class ThemeDto(
    val id: UUID?,
    val name: String,
    val image: String,
) {
    companion object {
        @JvmStatic
        fun toDto(from: Theme): ThemeDto {
            return ThemeDto(
                from.id,
                from.name,
                from.image,
            )
        }
    }
}
