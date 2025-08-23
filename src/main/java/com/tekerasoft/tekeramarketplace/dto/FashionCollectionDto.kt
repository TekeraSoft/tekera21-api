package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.FashionCollection
import java.util.UUID

data class FashionCollectionDto(
    val id: UUID?,
    val collectionName: String,
    val slug: String?,
    val image: String?,
    val products: List<ProductDto>,
    val description: String?,
) {
    companion object {
        @JvmStatic
        fun toDto(from: FashionCollection): FashionCollectionDto {
            return FashionCollectionDto(
                from.id,
                from.collectionName,
                from.slug,
                from.image,
                from.products.map { ProductDto.toDto(it) },
                from.description,
            )
        }
    }
}
