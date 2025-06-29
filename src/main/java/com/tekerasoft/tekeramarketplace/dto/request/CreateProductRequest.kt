package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.Attribute
import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import com.tekerasoft.tekeramarketplace.model.entity.CurrencyType
import com.tekerasoft.tekeramarketplace.model.entity.ProductType
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull

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
