package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.math.BigDecimal
import java.util.UUID

@Entity
open class BasketItem(
    open var productId: String,
    open var name: String,
    open var code: String? = null,
    open var brandName: String,
    open var quantity: Int,
    open var modelCode: String? = null,
    open var price: BigDecimal,
    open var sku: String? = null,
    open var barcode: String? = null,
    open var image: String,

    open var attributeId: String,

    @ElementCollection(fetch = FetchType.LAZY) // Lazy yükleme çoğu zaman daha iyidir
    @CollectionTable(name = "basket_item_attributes", // Bu collection için oluşturulacak yeni tablo adı
        joinColumns = [JoinColumn(name = "basket_item_id")]) // BasketItem'ı bu tabloya bağlayan sütun
    open var attributes: MutableList<BasketAttributes> = mutableListOf(),

    open var shippingPrice: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    open var company: Company,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_company_id")
    open var shippingCompany: ShippingCompany,

    ): BaseEntity()