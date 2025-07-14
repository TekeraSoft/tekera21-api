package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.FashionCollection
import java.util.UUID

data class FashionCollectionListDto(
    val id: UUID?,
    val collectionName: String,
    val slug: String?,
    val image: String?,
    val description: String?,
) {
    companion object {
        @JvmStatic
        fun toUiListDto(from: FashionCollection): FashionCollectionListDto {
            return FashionCollectionListDto(
                from.id,
                from.collectionName,
                from.slug,
                from.image,
                from.description
            )
        }
    }
}
