package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Category
import java.util.UUID

data class CategoryDto(
    val id: UUID?,
    val name: String,
    val slug: String,
    val image: String?,
    val subCategories: List<SubCategoryDto>
) {
    companion object {
        @JvmStatic
        fun toDto(category: Category): CategoryDto {
            return CategoryDto(
                category.id,
                category.name,
                category.slug,
                category.image,
                category.subCategories.map { SubCategoryDto(it.id,it.name, it.image) }
            )
        }
    }
}
