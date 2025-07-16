package com.tekerasoft.tekeramarketplace.model.entity

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "orders")
open class Order(

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name="user_id")
    open var user: User,

    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    open var basketItems: MutableList<BasketItem>,

    @Embedded
    @AttributeOverrides(
        AttributeOverride(name = "buildNo", column = Column(name = "shipping_build_no")),
        AttributeOverride(name = "city",     column = Column(name = "shipping_city")),
        AttributeOverride(name = "street",   column = Column(name = "shipping_street")),
        AttributeOverride(name = "postalCode", column = Column(name = "shipping_postal_code")),
        AttributeOverride(name = "doorNumber", column = Column(name = "shipping_door_number")),
        AttributeOverride(name = "detailAddress", column = Column(name = "shipping_detail_address")),
        AttributeOverride(name = "country",  column = Column(name = "shipping_country"))
    )
    open var shippingAddress: Address,

    @Embedded
    @AttributeOverrides(
    AttributeOverride(name = "buildNo", column = Column(name = "billing_build_no")),
    AttributeOverride(name = "city",     column = Column(name = "billing_city")),
    AttributeOverride(name = "street",   column = Column(name = "billing_street")),
    AttributeOverride(name = "postalCode", column = Column(name = "billing_postal_code")),
    AttributeOverride(name = "doorNumber", column = Column(name = "billing_door_number")),
    AttributeOverride(name = "detailAddress", column = Column(name = "billing_detail_address")),
    AttributeOverride(name = "country",  column = Column(name = "billing_country"))
    )
    open var billingAddress: Address? = null,

    open var totalPrice : BigDecimal,

    open var shippingPrice: BigDecimal,

    open var paymentType: PaymentType? = null,

    open var paymentStatus: PaymentStatus? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="shipping_company_id")
    open var shippingCompany: ShippingCompany,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    open var company: Company? = null,

): BaseEntity()
