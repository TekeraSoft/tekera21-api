package com.tekerasoft.tekeramarketplace.dto.request

data class AddToCartRequest(
  val productId: String,
  val variationId: String,
  val attributeId: String,
  val quantity: Int,
)
