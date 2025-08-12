package com.tekerasoft.tekeramarketplace.dto.request

import com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus
import com.tekerasoft.tekeramarketplace.model.enums.PaymentType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.math.BigDecimal

data class CreateOrderRequest(
    val buyer: BuyerRequest,
    @field:NotNull(message= "Sepette hiç ürün yok")
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
    @field:NotBlank("Alıcı adı zorunlu !")
    val name: String,
    @field:NotBlank("Alıcı soyad zorunlu !")
    val surname: String,
    @field:NotBlank("Alıcı mail zorunlu !")
    val email: String,
    @field:NotBlank("Alıcı gsm numarası zorunlu !")
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
    @field:NotBlank("Alıcı şehir zorunlu !")
    val city: String,
    @field:NotBlank("Alıcı mahalle/cadde zorunlu !")
    val street: String,
    val postalCode: String?,
    @field:NotBlank("Alıcı bina numarası zorunlu !")
    val buildNo: String,
    @field:NotBlank("Alıcı kapı numarası zorunlu !")
    val doorNumber: String,
    @field:NotBlank("Alıcı detaylı adres zorunlu !")
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