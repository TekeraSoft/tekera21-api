package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import com.tekerasoft.tekeramarketplace.model.enums.CurrencyType
import com.tekerasoft.tekeramarketplace.model.enums.ProductType

data class CreateProductRequest(

    @JsonProperty("name")
    val name: String,

    @JsonProperty("code")
    val code: String,

    @JsonProperty("brandName")
    val brandName: String,

    @JsonProperty("videoUrl")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val videoUrl: String? = null,

    @JsonProperty("description")
    val description: String,

    @JsonProperty("categoryId")
    val categoryId: String,

    @JsonProperty("companyId")
    val companyId: String,

    @JsonProperty("subCategories")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val subCategories: List<String>? = null,

    @JsonProperty("variants")
    val variants: List<VariationRequest>,

    @JsonProperty("currencyType")
    val currencyType: CurrencyType,

    @JsonProperty("tags")
    val tags: List<String>,

    @JsonProperty("productType")
    val productType: ProductType,

    @JsonProperty("attributeDetails")
    val attributeDetails: List<AttributeDetail>
)
