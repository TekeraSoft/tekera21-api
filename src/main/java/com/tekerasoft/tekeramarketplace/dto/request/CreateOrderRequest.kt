package com.tekerasoft.tekeramarketplace.dto.request

import com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus
import com.tekerasoft.tekeramarketplace.model.enums.PaymentType
import java.math.BigDecimal

data class CreateOrderRequest(
    val buyer: BuyerRequest,
    val basketItems: List<BasketItemRequest>,
    val shippingAddress: ShippingAddressRequest,
    val billingAddress: BillingAddressRequest?,
    val totalPrice: BigDecimal,
    val paymentType: PaymentType,
    val paymentStatus: PaymentStatus
) {
    companion object {
        @JvmStatic
        fun convertFromPayRequestToOrderRequest(payRequest: CreatePayRequest,
                                                totalPrice: BigDecimal,
                                                paymentType: PaymentType,
                                                paymentStatus: PaymentStatus): CreateOrderRequest {
            return CreateOrderRequest(

                payRequest.buyer.let { BuyerRequest(
                    it.name,
                    it.surname,
                    it.email,
                    it.gsmNumber,
                    it.identityNumber)},

                payRequest.basketItems.map { BasketItemRequest(
                    it.productId,
                    it.variationId,
                    it.attributeId,
                    it.quantity,
                    )},

                payRequest.shippingAddress.let { ShippingAddressRequest(
                    it.city,
                    it.street,
                    it.zipCode,
                    it.buildNo,
                    it.doorNumber,
                    it.address,
                    it.country,

                ) },
                payRequest.billingAddress?.let { BillingAddressRequest(
                    it.city,
                    it.street,
                    it.zipCode,
                    it.buildNo,
                    it.doorNumber,
                    it.address,
                    it.country,
                )},
                totalPrice,
                paymentType,
                paymentStatus
            )
        }
    }
}


data class BuyerRequest(
    val name: String,
    val surname: String,
    val email: String,
    val gsmNumber: String,
    val identityNumber: String,
)

data class BasketItemRequest(
    val productId: String,
    val variationId: String,
    val attributeId: String,
    val quantity: Int,
)

data class ShippingAddressRequest(
    val city: String,
    val street: String,
    val postalCode: String?,
    val buildNo: String,
    val doorNumber: String,
    val detailAddress: String,
    val country: String,
)

data class BillingAddressRequest(
    val city: String,
    val street: String,
    val postalCode: String?,
    val buildNo: String,
    val doorNumber: String,
    val detailAddress: String,
    val country: String,
)