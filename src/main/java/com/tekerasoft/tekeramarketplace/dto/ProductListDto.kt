package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import com.tekerasoft.tekeramarketplace.model.entity.CurrencyType
import com.tekerasoft.tekeramarketplace.model.entity.Product
import com.tekerasoft.tekeramarketplace.model.entity.ProductType
import java.util.UUID

data class ProductListDto(
    val id: UUID?,
    val name: String,
    val slug: String,
    val code: String?,
    val videoUrl: String?,
    val category: AdminCategoryDto?,
    val subCategories: List<AdminSubCategoryDto>?,
    val brandName: String,
    val description: String?,
    val variations: List<VariationDto>,
    val currencyType: CurrencyType,
    val tags: List<String>?,
    val productType: ProductType,
    val attributeDetails: List<AttributeDetail>,
    val rate: Double,
){
    companion object {
        @JvmStatic
        fun toDto(from: Product): ProductListDto {
            return ProductListDto(
                from.id,
                from.name,
                from.slug,
                from.code,
                from.videoUrl,
                from.category?.let { AdminCategoryDto(it.id, it.name,it.slug,it.image) },
                from.subCategories?.map { AdminSubCategoryDto(it.id, it.name,it.slug,it.image) },
                from.brandName,
                from.description,
                from.variations.map { it ->
                    VariationDto(
                        it.id,
                        it.modelName,
                        it.modelCode,
                        it.color,
                        it.attributes.map { it -> AttributeDto(
                            it.attributeDetails,
                            it.stock,
                            it.price,
                            it.discountPrice,
                            it.sku,
                            it.barcode
                        )},
                        it.images
                    )
                },
                from.currencyType,
                from?.tags,
                from.productType,
                from.attributes.map { AttributeDetail(it.key, it.value) },
                from.rate,
            )
        }
    }
}
