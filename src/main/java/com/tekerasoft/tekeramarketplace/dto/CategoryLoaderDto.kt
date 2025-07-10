package com.tekerasoft.tekeramarketplace.dto

open class CategoryLoaderDto(
    open var name: String? = null,
    open var slug: String? = null,
    open var subCategories: List<SubCategoryLoader> = emptyList(),
)

open class SubCategoryLoader(
    open var name: String? = null,
    open var slug: String? = null,
    open var children: List<SubCategoryLoader> = emptyList(),
)
