package com.tekerasoft.tekeramarketplace.builder;

import com.iyzipay.model.*;
import com.iyzipay.request.CreatePaymentRequest;
import com.tekerasoft.tekeramarketplace.dto.request.BasketItemRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreatePayRequest;
import com.tekerasoft.tekeramarketplace.model.entity.Attribute;
import com.tekerasoft.tekeramarketplace.model.entity.Order;
import com.tekerasoft.tekeramarketplace.model.entity.Product;
import com.tekerasoft.tekeramarketplace.service.AttributeService;
import com.tekerasoft.tekeramarketplace.service.ProductService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
public class PaymentRequestBuilder {

    private final ProductService productService;
    private final AttributeService attributeService;

    public PaymentRequestBuilder(ProductService productService, AttributeService attributeService) {
        this.productService = productService;
        this.attributeService = attributeService;
    }

    public CreatePaymentRequest build(CreatePayRequest req, Order order, String orderNumber, BigDecimal totalPrice, String callbackUrl) {
        CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
        paymentRequest.setLocale(Locale.TR.getValue());
        paymentRequest.setConversationId(orderNumber);
        paymentRequest.setCurrency(Currency.TRY.name());
        paymentRequest.setInstallment(1);
        paymentRequest.setBasketId(orderNumber);
        paymentRequest.setPaymentGroup(PaymentGroup.PRODUCT.name());

        // --- PaymentCard ---
        PaymentCard paymentCard = new PaymentCard();
        paymentCard.setCardNumber(req.getPaymentCard().getCardNumber());
        paymentCard.setCardHolderName(req.getPaymentCard().getCardHolderName());
        paymentCard.setExpireMonth(req.getPaymentCard().getExpireMonth());
        paymentCard.setExpireYear(req.getPaymentCard().getExpireYear());
        paymentCard.setCvc(req.getPaymentCard().getCvc());
        paymentCard.setRegisterCard(0);
        paymentRequest.setPaymentCard(paymentCard);

        // --- Buyer ---
        Buyer buyer = new Buyer();
        buyer.setId(order.getSellerOrder().get(0).getBuyer().getId().toString());
        buyer.setName(req.getBuyer().getName());
        buyer.setSurname(req.getBuyer().getSurname());
        buyer.setGsmNumber(req.getBuyer().getGsmNumber());
        buyer.setEmail(req.getBuyer().getEmail());
        buyer.setIdentityNumber(req.getBuyer().getIdentityNumber());
        buyer.setLastLoginDate(req.getBuyer().getLastLoginDate());
        buyer.setRegistrationDate(req.getBuyer().getRegistrationDate());
        buyer.setRegistrationAddress(req.getBuyer().getRegistrationAddress());
        if (req.getBillingAddress() != null) {
            buyer.setCity(req.getBillingAddress().getCity());
        }
        buyer.setCity(req.getShippingAddress().getCity());
        buyer.setCountry(req.getShippingAddress().getCountry());
        buyer.setZipCode(req.getShippingAddress().getZipCode());
        paymentRequest.setBuyer(buyer);

        // --- Shipping Address ---
        Address shippingAddress = new Address();
        shippingAddress.setContactName(req.getBuyer().getName() + " " + req.getBuyer().getSurname());
        shippingAddress.setCity(req.getShippingAddress().getCity());
        shippingAddress.setCountry(req.getShippingAddress().getCountry());
        shippingAddress.setAddress(req.getShippingAddress().getAddress());
        shippingAddress.setZipCode(req.getShippingAddress().getZipCode());
        paymentRequest.setShippingAddress(shippingAddress);

        // --- Billing Address ---
        if (req.getBillingAddress() != null) {
            Address billingAddress = new Address();
            billingAddress.setContactName(req.getBuyer().getName() + " " + req.getBuyer().getSurname());
            billingAddress.setCity(req.getBillingAddress().getCity());
            billingAddress.setCountry(req.getBillingAddress().getCountry());
            billingAddress.setAddress(req.getBillingAddress().getAddress());
            billingAddress.setZipCode(req.getBillingAddress().getZipCode());
            paymentRequest.setBillingAddress(billingAddress);
        } else {
            paymentRequest.setBillingAddress(shippingAddress);
        }

        // --- Basket Items ---
        List<BasketItem> basketItems = new ArrayList<>();
        for (BasketItemRequest bi : req.getBasketItems()) {
            com.iyzipay.model.BasketItem basketItem = new com.iyzipay.model.BasketItem();
            Attribute productAttribute = attributeService.getAttributeById(bi.getAttributeId());
            Product product = productService.getById(UUID.fromString(bi.getProductId()));
            basketItem.setId(bi.getProductId());
            basketItem.setName(product.getName());
            basketItem.setCategory1(product.getCategory().getName());
            basketItem.setCategory2(product.getSubCategories().stream().findFirst().get().getName());
            basketItem.setItemType(BasketItemType.PHYSICAL.name());

            BigDecimal totalItemPrice = productAttribute.getPrice()
                    .multiply(new BigDecimal(bi.getQuantity()));

            basketItem.setPrice(totalItemPrice);
            basketItems.add(basketItem);
        }

        paymentRequest.setCallbackUrl(callbackUrl);
        paymentRequest.setPrice(totalPrice);
        paymentRequest.setPaidPrice(totalPrice);
        paymentRequest.setBasketItems(basketItems);

        return paymentRequest;
    }

}
