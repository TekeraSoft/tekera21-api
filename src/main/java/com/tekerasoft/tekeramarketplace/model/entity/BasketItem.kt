package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.config.NoArg
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
open class BasketItem(
    open var productId: String,
    open var variationId: String,
    open var attributeId: String,
    open var name: String,
    open var slug: String,
    open var code: String? = null,
    open var brandName: String,
    open var quantity: Int,
    open var modelCode: String? = null,
    open var price: BigDecimal,
    open var sku: String? = null,
    open var barcode: String? = null,
    open var image: String,

    @ElementCollection(fetch = FetchType.LAZY) // Lazy yükleme çoğu zaman daha iyidir
    @CollectionTable(name = "basket_item_attributes", // Bu collection için oluşturulacak yeni tablo adı
        joinColumns = [JoinColumn(name = "basket_item_id")]) // BasketItem'ı bu tabloya bağlayan sütun
    open var attributes: MutableList<BasketAttributes> = mutableListOf(),

    open var shippingPrice: BigDecimal,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id")
    open var seller: Seller,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_company_id")
    open var shippingCompany: ShippingCompany,

    ): BaseEntity() {
        constructor() : this(
            productId = "",
            variationId = "",
            attributeId = "",
            name = "",
            slug = "",
            code = null,
            brandName = "",
            quantity = 0,
            modelCode = "",
            price = BigDecimal.ZERO,
            sku = "",
            barcode = "",
            image = "",
            attributes = mutableListOf(),
            shippingPrice = BigDecimal.ZERO,
            seller = Seller(),
            shippingCompany = ShippingCompany(),
        )
    }