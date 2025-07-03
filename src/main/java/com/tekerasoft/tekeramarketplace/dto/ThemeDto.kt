package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Theme

data class ThemeDto(
    val name: String,
    val image: String
) {
    companion object {
        @JvmStatic
        fun toDto(from: Theme): ThemeDto {
            return ThemeDto(
                from.name,
                from.image
            )
        }
    }
}
