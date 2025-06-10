package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.CurrencyType
import com.tekerasoft.tekeramarketplace.model.entity.Product
import com.tekerasoft.tekeramarketplace.model.entity.ProductType
import java.util.UUID

data class ProductDto(
    val id: UUID?,
    val name: String,
    val slug: String,
    val code: String,
    val brandName: String,
    val company: ProductCompanyDto,
    val description: String,
    val variations: List<VariationDto>,
    val currencyType: CurrencyType,
    val tags: List<String>,
    val productType: ProductType,
    val attributes: List<AttributeDto>,
    val rate: Double,
    val comments: List<CommentDto>
) {
    companion object {
        @JvmStatic
        fun toDto(from: Product): ProductDto {
            return ProductDto(
                from.id,
                from.name,
                from.slug,
                from.code,
                from.brandName,
                from.company.let {
                    ProductCompanyDto(it.name,it.logo, it.rate ) },
                from.description,
                from.variations.map {
                    VariationDto(
                        it.id,
                        it.modelName,
                        it.modelCode,
                        it.price,
                        it.stock,
                        it.sku,
                        it.barcode,
                        it.attributes.map { AttributeDto(it.key,it.value)},
                        it.images
                    )
                },
                from.currencyType,
                from.tags.toList(),
                from.productType,
                from.attributes.map { AttributeDto(it.key, it.value) },
                from.rate,
                from.comments.map { CommentDto(it.id,it.userName,it.rate,it.message) }
            )
        }
    }
}
