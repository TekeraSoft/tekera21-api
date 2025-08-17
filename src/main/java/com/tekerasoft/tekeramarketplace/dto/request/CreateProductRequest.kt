package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import com.tekerasoft.tekeramarketplace.model.enums.CurrencyType
import com.tekerasoft.tekeramarketplace.model.enums.ProductType
import jakarta.validation.constraints.NotBlank

data class CreateProductRequest(

    @JsonProperty("name")
    @field:NotBlank(message = "Product Name cannot be blank")
    val name: String,

    @JsonProperty("code")
    @field:NotBlank(message = "Product Code cannot be blank")
    val code: String,

    @JsonProperty("brandName")
    @field:NotBlank(message = "Product Brand Name cannot be blank")
    val brandName: String,

    @JsonProperty("description")
    @field:NotBlank(message = "Description cannot be blank")
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
