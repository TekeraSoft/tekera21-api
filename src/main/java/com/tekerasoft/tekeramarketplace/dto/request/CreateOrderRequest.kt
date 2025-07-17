package com.tekerasoft.tekeramarketplace.dto.request

import com.tekerasoft.tekeramarketplace.model.entity.Address
import com.tekerasoft.tekeramarketplace.model.entity.AttributeDetail
import com.tekerasoft.tekeramarketplace.model.entity.BasketItem
import com.tekerasoft.tekeramarketplace.model.entity.Buyer
import com.tekerasoft.tekeramarketplace.model.entity.PaymentStatus
import com.tekerasoft.tekeramarketplace.model.entity.PaymentType
import java.math.BigDecimal

data class CreateOrderRequest(
    val buyer: BuyerRequest,
    val basketItems: List<BasketItemRequest>,
    val shippingAddress: ShippingAddressRequest,
    val billingAddress: BillingAddressRequest,
    val totalPrice: BigDecimal,
    val shippingPrice: BigDecimal,
    val paymentType: PaymentType,
    val paymentStatus: PaymentStatus,
    val shippingCompanyId: String,
    val companyId: String,
)


data class BuyerRequest(
    val name: String,
    val surname: String,
    val email: String,
    val gsmNumber: String,
    val identityNumber: String,
) {
    companion object {
        @JvmStatic
        fun toEntity(from: BuyerRequest, isRegistered: Boolean): Buyer {
            return Buyer(
                from.name,
                from.surname,
                from.email,
                from.gsmNumber,
                from.identityNumber,
                isRegistered,
            )
        }
    }
}

data class BasketItemRequest(
    val name: String,
    val slug: String,
    val code: String,
    val brandName: String,
    val quantity: Int,
    val modelName: String,
    val modelCode: String,
    val price: BigDecimal,
    val sku: String,
    val barcode: String,
    val attributes: List<AttributeDetailRequest>,
    val image: String,
    val companyId: String,
) {
    companion object {
        @JvmStatic
        fun toEntity(from: BasketItemRequest): BasketItem {
            return BasketItem(
                from.name,
                 from.slug,
                from.code,
                from.brandName,
                from.quantity,
                from.modelName,
                from.modelCode,
                from.price,
                from.sku,
                from.barcode,
                from.attributes.map { it -> AttributeDetail(it.key, it.value) },
                from.image,
                from.companyId,
            )
        }
    }
}

data class AttributeDetailRequest(
    val key: String,
    val value: String,
)

data class ShippingAddressRequest(
    val city: String,
    val street: String,
    val postalCode: String,
    val buildNo: String,
    val doorNumber: String,
    val detailAddress: String,
    val country: String,
) {
    companion object {
        @JvmStatic
        fun toEntity(from: ShippingAddressRequest): Address {
            return Address(
                from.city,
                from.street,
                    from.postalCode,
                from.buildNo,
                from.doorNumber,
                from.detailAddress,
                from.country,
            )
        }
    }
}

data class BillingAddressRequest(
    val city: String,
    val street: String,
    val postalCode: String,
    val buildNo: String,
    val doorNumber: String,
    val detailAddress: String,
    val country: String,
) {
    companion object {
        @JvmStatic
        fun toEntity(from: BillingAddressRequest): Address {
            return Address(
                from.city,
                from.street,
                from.postalCode,
                from.buildNo,
                from.doorNumber,
                from.detailAddress,
                from.country,
            )
        }
    }
}