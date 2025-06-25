package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import com.tekerasoft.tekeramarketplace.model.entity.CurrencyType
import com.tekerasoft.tekeramarketplace.model.entity.ProductType

data class UpdateProductRequest(

    @JsonProperty("id")
    val id: String,

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

    @JsonProperty("subCategories")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val subCategories: List<String>? = null,

    @JsonProperty("variants")
    val variants: List<VariationUpdateRequest>,

    @JsonProperty("currencyType")
    val currencyType: CurrencyType,

    @JsonProperty("tags")
    val tags: List<String>,

    @JsonProperty("productType")
    val productType: ProductType,

    @JsonProperty("attributes")
    val attributeDetails: List<AttributeDetail>,

    @JsonProperty("deleteImages")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val deleteImages: List<String>? = listOf(),
)
