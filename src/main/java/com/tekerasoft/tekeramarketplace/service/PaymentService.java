package com.tekerasoft.tekeramarketplace.service;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.model.Address;
import com.iyzipay.model.Buyer;
import com.iyzipay.model.Currency;
import com.iyzipay.model.Locale;
import com.iyzipay.request.CreatePaymentRequest;
import com.iyzipay.request.CreateThreedsPaymentRequestV2;
import com.tekerasoft.tekeramarketplace.dto.request.BasketItemRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreateOrderRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreatePayRequest;
import com.tekerasoft.tekeramarketplace.exception.PaymentException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class PaymentService {

    private final ProductService productService;

    @Value("${spring.origin.url}")
    private String originUrl;

    @Value("${spring.iyzico.callback_url}")
    private String paymentCallbackUrl;

    private final Options options;
    private final OrderService orderService;
    private final AttributeService attributeService;
    private static final Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(Options options, OrderService orderService, AttributeService attributeService,
                          ProductService productService) {
        this.options = options;
        this.orderService = orderService;
        this.attributeService = attributeService;
        this.productService = productService;
    }

    // Kargo ücreti iki taraf için ayrı gösterilecek

    public ThreedsInitialize payment(CreatePayRequest req) {
        try {
            // Calculate total price map !
            BigDecimal totalPrice = req.getBasketItems().stream()
                    .map(bi -> {
                        Attribute productAttribute = attributeService.getAttributeById(bi.getAttributeId());
                        BigDecimal price = productAttribute.getPrice(); // price BigDecimal olmalı
                        BigDecimal quantity = BigDecimal.valueOf(bi.getQuantity()); // quantity int/long ise çeviriyoruz
                        return price.multiply(quantity);
                    }).reduce(BigDecimal.ZERO, BigDecimal::add)
                    .setScale(2, RoundingMode.HALF_UP);

            Order order = orderService.createOrder(
                    CreateOrderRequest.convertFromPayRequestToOrderRequest(
                            req,
                            totalPrice,
                            PaymentType.CREDIT_CARD,
                            PaymentStatus.PENDING
                    )
            );

            CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
            paymentRequest.setLocale(Locale.TR.getValue());
            paymentRequest.setConversationId(order.getId().toString());
            paymentRequest.setCurrency(Currency.TRY.name());
            paymentRequest.setInstallment(1);
            paymentRequest.setBasketId(order.getId().toString());
            paymentRequest.setPaymentGroup(PaymentGroup.PRODUCT.name());

            PaymentCard paymentCard = new PaymentCard();
            paymentCard.setCardNumber(req.getPaymentCard().getCardNumber());
            paymentCard.setCardHolderName(req.getPaymentCard().getCardHolderName());
            paymentCard.setExpireMonth(req.getPaymentCard().getExpireMonth());
            paymentCard.setExpireYear(req.getPaymentCard().getExpireYear());
            paymentCard.setCvc(req.getPaymentCard().getCvc());
            paymentCard.setRegisterCard(0);
            paymentRequest.setPaymentCard(paymentCard);

            Buyer buyer = new Buyer();
            buyer.setId(order.getBuyer().getId().toString());
            buyer.setName(req.getBuyer().getName());
            buyer.setSurname(req.getBuyer().getSurname());
            buyer.setGsmNumber(req.getBuyer().getGsmNumber());
            buyer.setEmail(req.getBuyer().getEmail());
            buyer.setIdentityNumber(req.getBuyer().getIdentityNumber());
            buyer.setLastLoginDate(req.getBuyer().getLastLoginDate());
            buyer.setRegistrationDate(req.getBuyer().getRegistrationDate());
            buyer.setRegistrationAddress(req.getBuyer().getRegistrationAddress());
            buyer.setIp(req.getBuyer().getIp());
            //buyer.setCity(req.getShippingAddress().getCity());
            buyer.setCity("Antalya");
            buyer.setCountry(req.getShippingAddress().getCountry());
            buyer.setZipCode(req.getShippingAddress().getZipCode());
            paymentRequest.setBuyer(buyer);

            Address shippingAddress = new Address();
            shippingAddress.setContactName(req.getBuyer().getName()+" "+req.getBuyer().getSurname());
            shippingAddress.setCity(req.getShippingAddress().getCity());
            shippingAddress.setCountry(req.getShippingAddress().getCountry());
            shippingAddress.setAddress(req.getShippingAddress().getAddress());
            shippingAddress.setZipCode(req.getShippingAddress().getZipCode());
            paymentRequest.setShippingAddress(shippingAddress);

            if(req.getBillingAddress() != null) {
                Address billingAddress = new Address();
                billingAddress.setContactName(req.getBuyer().getName()+" "+req.getBuyer().getSurname());
                billingAddress.setCity(req.getBillingAddress().getCity());
                billingAddress.setCountry(req.getBillingAddress().getCountry());
                billingAddress.setAddress(req.getBillingAddress().getAddress());
                billingAddress.setZipCode(req.getBillingAddress().getZipCode());
                paymentRequest.setBillingAddress(billingAddress);
            }else {
                paymentRequest.setBillingAddress(shippingAddress);
            }

            List<com.iyzipay.model.BasketItem> basketItems = new ArrayList<>();
            for(BasketItemRequest bi: req.getBasketItems()) {
                com.iyzipay.model.BasketItem basketItem = new com.iyzipay.model.BasketItem();
                Attribute productAttribute = attributeService.getAttributeById(bi.getAttributeId());
                Product product = productService.getById(UUID.fromString(bi.getProductId()));
                basketItem.setId(bi.getProductId());
                basketItem.setName(product.getName());
                basketItem.setCategory1(product.getCategory().getName());
                basketItem.setCategory2(product.getSubCategories().stream().findFirst().get().getName());
                basketItem.setItemType(product.getProductType().name());
                basketItem.setItemType(BasketItemType.PHYSICAL.name());

                BigDecimal totalItemPrice = new BigDecimal(String.valueOf(productAttribute.getPrice()));
                System.out.println(totalItemPrice);
                basketItem.setPrice(totalItemPrice.setScale(2, RoundingMode.HALF_UP));
                basketItems.add(basketItem);
            }


            paymentRequest.setCallbackUrl(paymentCallbackUrl);

            paymentRequest.setPrice(totalPrice);
            paymentRequest.setPaidPrice(totalPrice);
            paymentRequest.setBasketItems(basketItems);
            System.out.println(paymentRequest);
            return ThreedsInitialize.create(paymentRequest,options);
        } catch (RuntimeException e) {
            throw new RuntimeException("Error Creating Payment Request",e);
        }


    }

    public ThreedsPayment completePayment(String paymentId, String conversationId) {
        try {
            CreateThreedsPaymentRequestV2 threedsPaymentRequestV2 = new CreateThreedsPaymentRequestV2();
            threedsPaymentRequestV2.setPaymentId(paymentId);
            threedsPaymentRequestV2.setConversationId(conversationId);
            threedsPaymentRequestV2.setLocale(Locale.TR.getValue());

            return ThreedsPayment.createV2(threedsPaymentRequestV2, options);
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error completing 3D Secure Payment");
        }
    }

    public String paymentCheck (HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String[]> parameters =  request.getParameterMap();
            String paymentId = parameters.containsKey("paymentId") ? parameters.get("paymentId")[0] : null;
            String conversationId = parameters.containsKey("conversationId") ? parameters.get("conversationId")[0] : null;

            ThreedsPayment threedsPayment = completePayment(paymentId, conversationId);

            if(paymentId == null || conversationId == null) {
                throw new PaymentException("Eksik Ödeme Bilgisi");
            }

            if("success".equalsIgnoreCase(threedsPayment.getStatus())) {
                orderService.completeOrder(conversationId, PaymentStatus.PAID);
                response.sendRedirect(originUrl + "/payment-success");
            } else {
                orderService.completeOrder(conversationId, PaymentStatus.FAIL);
                response.sendRedirect(originUrl + "/payment-failure");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new PaymentException("Error completing 3D Secure Payment");
        }
        return null;
    }

}
