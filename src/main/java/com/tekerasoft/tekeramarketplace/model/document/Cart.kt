package com.tekerasoft.tekeramarketplace.model.document

import com.tekerasoft.tekeramarketplace.config.NoArg
import jakarta.persistence.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.math.BigDecimal

@Document(collection = "carts")
data class Cart(
    @Id
    var id: String,
    var cartItems: MutableList<CartItem> = mutableListOf(),
    var totalPrice: BigDecimal,
    var itemCount: Int
): Serializable {
    constructor() : this("", mutableListOf(), BigDecimal.ZERO, 0)
}


data class CartItem(
    var productId: String,
    var variationId: String,
    var attributeId: String,
    var productSlug: String,
    var name: String,
    var color: String,
    var modelCode: String,
    var quantity: Int,
    var price: BigDecimal,
    var brandName: String,
    var image: String,
    var attributes: MutableList<CartAttributes>
): Serializable {
    constructor(): this(
        "",
        "",
        "",
        "",
        "",
        "",
        "",
        0,
        BigDecimal.ZERO,
        "",
        "",
        mutableListOf()
    )
}

data class CartAttributes(
    val key: String,
    val value: String
) : Serializable

