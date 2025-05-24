package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.Attribute
import com.tekerasoft.tekeramarketplace.model.entity.CurrencyType
import com.tekerasoft.tekeramarketplace.model.entity.ProductType

data class CreateProductRequest(
    @JsonProperty("name")
    val name: String,
    @JsonProperty("code")
    val code: String,
    @JsonProperty("brandName")
    val brandName: String,
    @JsonProperty("description")
    val description: String,
    @JsonProperty("categoryId")
    val categoryId: String,
    @JsonProperty("categoryId")
    val companyId: String,
    @JsonProperty("subCategories")
    val subCategories: List<String>,
    @JsonProperty("variants")
    val variants: List<VariationRequest>,
    @JsonProperty("currencyType")
    val currencyType: CurrencyType,
    @JsonProperty("tags")
    val tags: List<String>,
    @JsonProperty("productType")
    val productType: ProductType,
    @JsonProperty("attributes")
    val attributes: List<Attribute>
)
