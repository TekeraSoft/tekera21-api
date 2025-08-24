package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Product
import com.tekerasoft.tekeramarketplace.model.entity.User
import com.tekerasoft.tekeramarketplace.model.enums.CurrencyType
import com.tekerasoft.tekeramarketplace.model.enums.LikeState
import java.math.BigDecimal
import java.util.UUID

data class ProductUiDto(
    val id: UUID?,
    val name: String,
    val slug: String,
    val brandName: String?,
    val category: String?,
    val seller: ProductCompanyDto?,
    val subCategories: List<SubCategoryDto>?,
    val variations: List<VariationUiDto>,
    val currencyType: CurrencyType,
    val videoUrl: String?,
    val tags: List<String>?,
    val price: BigDecimal?,
    val discountPrice: BigDecimal?,
    val rate: Double,
    val likeCount: Int?,
    val description: String?,
    val hasFollowed: Boolean?,
    val hasFavorite: Boolean?,
    val hasLiked: LikeState?,
) {
    companion object {
        @JvmStatic
        fun toProductUiDto(product: Product,user: User?): ProductUiDto {
            return ProductUiDto(
                product.id,
                product.name,
                product.slug,
                product.brandName,
                product.category?.name,
                product.seller.let { ProductCompanyDto(it.id, it.name, it.logo, it.rate) },
                product.subCategories?.map { it -> SubCategoryDto.toDto(it) },
                product.variations.map { it ->
                    VariationUiDto(
                        it.id,
                        it.modelName,
                        it.modelCode,
                        it.color,
                        it.images)
                },
                product.currencyType,
                product.videoUrl,
                product.tags,
                product.variations.firstOrNull()?.attributes?.firstOrNull()?.price,
                product.variations.firstOrNull()?.attributes?.firstOrNull()?.discountPrice,
                product.rate,
                product.likeCount,
                product.description,
                product.seller.followUsers.contains(user),
                user?.favProducts?.contains(product),
                user?.likedProducts?.find { p -> p.product == product }?.state
            )
        }
    }
}

data class VariationUiDto(
    val id: UUID?,
    val modelName: String,
    val modelCode: String,
    val color: String,
    val images: List<String>,
)
