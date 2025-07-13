package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.tekerasoft.tekeramarketplace.dto.AttributeDto
import com.tekerasoft.tekeramarketplace.model.entity.Attribute
import jakarta.validation.constraints.NotBlank
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class VariationRequest(

    @JsonProperty("modelName")
    val modelName: String,

    @JsonProperty("modelCode")
    val modelCode: String,

    @JsonProperty("color")
    val color: String,

    @JsonProperty("attributes")
    val attributes: List<CreateAttributeRequest>,

    @JsonProperty("subCategories")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val imageUrls: List<String>? = null,
)
