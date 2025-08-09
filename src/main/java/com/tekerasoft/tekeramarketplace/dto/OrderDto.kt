package com.tekerasoft.tekeramarketplace.dto

import com.tekerasoft.tekeramarketplace.model.entity.Order
import java.math.BigDecimal

open class OrderDto(
    open var orderNo: String,
    open var shippingPrice: BigDecimal,
    open var totalPrice: BigDecimal,
    open var sellerOrders: MutableList<SellerOrderDto>
) {

}
