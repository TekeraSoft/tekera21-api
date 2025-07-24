package com.tekerasoft.tekeramarketplace.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.math.BigDecimal

data class CreatePayRequest(
    @JsonProperty("paymentCard")
    val paymentCard: PaymentCard,
    @JsonProperty("buyer")
    val buyer: Buyer,
    @JsonProperty("shippingAddress")
    val shippingAddress: Address,
    @JsonProperty("basketItems")
    val basketItems: List<BasketItemRequest>,
    @JsonProperty("billingAddress")
    val billingAddress: Address,
    @JsonProperty("shippingPrice")
    val shippingPrice: BigDecimal
)

data class PaymentCard(
    @JsonProperty("cardHolderName")
    val cardHolderName: String,
    @JsonProperty("cardNumber")
    val cardNumber: String,
    @JsonProperty("expireMonth")
    val expireMonth: String,
    @JsonProperty("expireYear")
    val expireYear: String,
    @JsonProperty("cvc")
    val cvc: String,
)

data class Buyer(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("name")
    val name: String,
    @JsonProperty("surname")
    val surname: String,
    @JsonProperty("gsmNumber")
    val gsmNumber: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("ip")
    val ip: String,
    @JsonProperty("identityNumber")
    val identityNumber: String,
    @JsonProperty("lastLoginDate")
    val lastLoginDate: String,
    @JsonProperty("registrationDate")
    val registrationDate: String,
    @JsonProperty("registrationAddress")
    val registrationAddress: String,
)

data class Address(
    @JsonProperty("contactName")
    val contactName: String,
    @JsonProperty("city")
    val city: String,
    @JsonProperty("state")
    val state: String,
    @JsonProperty("buildNo")
    val buildNo: String,
    @JsonProperty("buildNo")
    val doorNumber: String,
    @JsonProperty("country")
    val country: String,
    @JsonProperty("address")
    val address: String,
    @JsonProperty("street")
    val street: String,
    @JsonProperty("zipCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val zipCode: String? = null,
)

data class BasketItem(

    @JsonProperty("id")
    val id: String,

    @JsonProperty("name")
    val name: String,

    @JsonProperty("code")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val code: String? = null,

    @JsonProperty("brandName")
    val brandName: String,

    @JsonProperty("category1")
    val category1: String,

    @JsonProperty("category2")
    val category2: String,

    @JsonProperty("price")
    var price: BigDecimal,

    @JsonProperty("quantity")
    val quantity: Int,

    @JsonProperty("modelCode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val modelCode: String? = null,

    @JsonProperty("sku")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val sku: String? = null,

    @JsonProperty("barcode")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val barcode: String? = null,

    @JsonProperty("image")
    val image: String,

    @JsonProperty("companyId")
    val companyId: String,
)

