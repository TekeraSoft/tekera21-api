package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.StockAttribute

data class AttributeDto(
    val stockAttribute: MutableList<StockAttribute>,
    val stock: Int
)
