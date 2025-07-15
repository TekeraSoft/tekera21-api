package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import com.tekerasoft.tekeramarketplace.model.entity.CurrencyType
import com.tekerasoft.tekeramarketplace.model.entity.Product
import com.tekerasoft.tekeramarketplace.model.entity.ProductType
import java.math.BigDecimal
import java.util.UUID

data class ProductUiDto(
    val id: UUID?,
    val name: String,
    val slug: String,
    val brandName: String?,
    val variations: List<VariationUiDto>,
    val currencyType: CurrencyType,
    val videoUrl: String?,
    val tags: List<String>?,
    val price: BigDecimal?,
    val discountPrice: BigDecimal?,
    val rate: Double,
) {
    companion object {
        @JvmStatic
        fun toProductUiDto(product: Product): ProductUiDto {
            return ProductUiDto(
                product.id,
                product.name,
                product.slug,
                product.brandName,
                product.variations.map { it ->
                    VariationUiDto(
                        it.id,
                        it.modelName,
                        it.modelCode,
                        it.color,
                        it.images.firstOrNull()?.let { listOf(it) } ?: emptyList())
                },
                product.currencyType,
                product.videoUrl,
                product.tags,
                product.variations.firstOrNull()?.attributes?.firstOrNull()?.price,
                product.variations.firstOrNull()?.attributes?.firstOrNull()?.discountPrice,
                product.rate
            )
        }
    }
}

data class VariationUiDto(
    val id: UUID?,
    val modelName: String,
    val modelCode: String,
    val color: String,
    val images: List<String>?,
)
