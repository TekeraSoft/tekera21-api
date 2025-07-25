package com.tekerasoft.tekeramarketplace.dto.request

import com.tekerasoft.tekeramarketplace.model.entity.Address
import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import com.tekerasoft.tekeramarketplace.model.entity.BasketItem
import com.tekerasoft.tekeramarketplace.model.entity.Buyer
import com.tekerasoft.tekeramarketplace.model.entity.Company
import com.tekerasoft.tekeramarketplace.model.entity.Order
import com.tekerasoft.tekeramarketplace.model.entity.PaymentStatus
import com.tekerasoft.tekeramarketplace.model.entity.PaymentType
import com.tekerasoft.tekeramarketplace.model.entity.ShippingCompany
import java.math.BigDecimal
import java.util.UUID

data class CreateOrderRequest(
    val buyer: BuyerRequest,
    val basketItems: List<BasketItemRequest>,
    val shippingAddress: ShippingAddressRequest,
    val billingAddress: BillingAddressRequest,
    val totalPrice: BigDecimal,
    val shippingPrice: BigDecimal,
    val paymentType: PaymentType,
    val paymentStatus: PaymentStatus
) {
    companion object {
        @JvmStatic
        fun convertFromPayRequestToOrderRequest(payRequest: CreatePayRequest,
                                                totalPrice: BigDecimal,
                                                shippingPrice: BigDecimal,
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
                    it.name,
                    it.code,
                    it.brandName,
                    it.quantity,
                    it.modelCode,
                    it.price,
                    it.sku,
                    it.barcode,
                    it.image,
                    it.companyId,
                    it.shippingCompanyId
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
                payRequest.billingAddress.let { BillingAddressRequest(
                    it.city,
                    it.street,
                    it.zipCode,
                    it.buildNo,
                    it.doorNumber,
                    it.address,
                    it.country,
                )},
                totalPrice,
                shippingPrice,
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
    val productId: UUID,
    val name: String,
    val code: String?,
    val brandName: String,
    val quantity: Int,
    val modelCode: String?,
    val price: BigDecimal,
    val sku: String?,
    val barcode: String?,
    val image: String,
    val companyId: String,
    val shippingCompanyId: String
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