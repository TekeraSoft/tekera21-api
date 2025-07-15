package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.SubCategory
import java.util.UUID

data class SubCategoryDto(
    val id: UUID?,
    val name: String,
    val image: String?,
    val children: MutableList<SubCategoryDto>,
) {
    companion object {
        @JvmStatic
        fun toDto(from: SubCategory): SubCategoryDto {
            return SubCategoryDto(
                from.id,
                from.name,
                from.image,
                from.children.map { it -> SubCategoryDto.toDto(it) }.toMutableList(),
            )
        }
    }
}
