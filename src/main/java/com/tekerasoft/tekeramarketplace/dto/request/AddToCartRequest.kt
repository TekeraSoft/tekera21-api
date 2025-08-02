package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class AddToCartRequest(
  @JsonProperty("productId")
  val productId: String,
  @JsonProperty("variationId")
  val variationId: String,
  @JsonProperty("attributeId")
  val attributeId: String,
  @JsonProperty("quantity")
  val quantity: Int,
)
