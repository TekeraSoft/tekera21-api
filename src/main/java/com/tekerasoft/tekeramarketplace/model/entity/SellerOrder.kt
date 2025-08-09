package com.tekerasoft.tekeramarketplace.model.entity

import com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus
import com.tekerasoft.tekeramarketplace.model.enums.PaymentType
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.JoinTable
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.math.BigDecimal

@Entity
@Table(name= "seller_orders")
open class SellerOrder(

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id")
    open var user: User? = null,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "buyer_id")
    open var buyer: Buyer,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true) // FetchType.LAZY varsayılan olarak gelir
    @JoinTable(
        name = "order_basket_items", // İlişki için oluşturulacak ara tablo adı
        joinColumns = [JoinColumn(name = "order_id")], // Bu tablonun Order'a referans veren sütunu
        inverseJoinColumns = [JoinColumn(name = "basket_item_id")] // Bu tablonun BasketItem'a referans veren sütunu
    )
    open var basketItems: MutableList<BasketItem> = mutableListOf(),

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var shippingAddress: Address,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    open var billingAddress: Address? = null,

    open var totalPrice : BigDecimal,

    open var shippingPrice: BigDecimal,

    open var paymentType: PaymentType? = null,

    open var paymentStatus: PaymentStatus? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    open var order: Order? = null,

): BaseEntity() {
    constructor() : this(
        user = null,
        buyer = Buyer(),
        shippingAddress = Address(),
        billingAddress = null,
        totalPrice = BigDecimal.ZERO,
        shippingPrice = BigDecimal.ZERO,
        paymentType = null,
        paymentStatus = null
    )
}
