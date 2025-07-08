package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.FashionCollection
import java.util.UUID

data class FashionCollectionDto(
    val id: UUID?,
    val collectionName: String,
    val slug: String?,
    val products: List<ProductDto>,
    val image: String?,
    val description: String?,
    val isActive: Boolean?
) {
    companion object {
        @JvmStatic
        fun toDto(from: FashionCollection): FashionCollectionDto {
            return FashionCollectionDto(
                from.id,
                from.collectionName,
                from.slug,
                from.products.map(ProductDto::toDto).toList(),
                from.image,
                from.description,
                from.isActive
            )
        }
    }
}
