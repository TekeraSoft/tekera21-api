package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.Attribute
import com.tekerasoft.tekeramarketplace.model.CurrencyType
import com.tekerasoft.tekeramarketplace.model.ProductType

data class CreateProductRequest(
    @JsonProperty("name")
    val name: String,
    val code: String,
    val description: String,
    val categoryId: String,
    val subCategories: List<String>,
    val variants: List<VariationRequest>,
    val currencyType: CurrencyType,
    val tags: List<String>,
    val productType: ProductType,
    val attributes: List<Attribute>
)
