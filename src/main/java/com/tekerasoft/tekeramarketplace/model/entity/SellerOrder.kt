package com.tekerasoft.tekeramarketplace.model.entity

open class SellerOrder(
    open var orderNo: String,
    open var seller: Seller,
    open var buyer: Buyer,
    //open var sellerOrder: SellerOrder,
): BaseEntity()
