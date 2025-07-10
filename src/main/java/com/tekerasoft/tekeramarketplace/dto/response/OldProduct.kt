package com.tekerasoft.tekeramarketplace.dto.response

import java.math.BigDecimal

open class OldProduct(
    open var name: String,
    open var slug: String,
    open var description: String,
    open var price: BigDecimal,
    open var length: String,
    open var colorSize: List<ColorSize>,
    open var discountPrice: BigDecimal
    )


open class ColorSize(
    open var color: String,
    open var stockSize: List<StockSize>,
    open var stockCode: String,
    open var images: List<String>
)

open class StockSize(
    open var size: String,
    open var stock: Int,

)