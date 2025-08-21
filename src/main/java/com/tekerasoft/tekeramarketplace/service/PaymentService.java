package com.tekerasoft.tekeramarketplace.service;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.model.Locale;
import com.iyzipay.request.CreatePaymentRequest;
import com.iyzipay.request.CreateThreedsPaymentRequest;
import com.tekerasoft.tekeramarketplace.builder.PaymentRequestBuilder;
import com.tekerasoft.tekeramarketplace.dto.request.CreatePayRequest;
import com.tekerasoft.tekeramarketplace.exception.PaymentException;
import com.tekerasoft.tekeramarketplace.model.entity.*;
import com.tekerasoft.tekeramarketplace.model.enums.PaymentResult;
import com.tekerasoft.tekeramarketplace.model.enums.PaymentStatus;
import com.tekerasoft.tekeramarketplace.model.enums.PaymentType;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class PaymentService {

    private final SellerOrderService sellerOrderService;
    private final AuthenticationFacade authenticationFacade;

    @Value("${spring.iyzico.callback_url}")
    private String paymentCallbackUrl;

    private final Options options;
    private final OrderService orderService;
    private final AttributeService attributeService;
    private final PaymentRequestBuilder  paymentRequestBuilder;

    public PaymentService(Options options, OrderService orderService, AttributeService attributeService,
                           SellerOrderService sellerOrderService,
                          PaymentRequestBuilder paymentRequestBuilder,
                          AuthenticationFacade authenticationFacade) {
        this.options = options;
        this.orderService = orderService;
        this.attributeService = attributeService;
        this.sellerOrderService = sellerOrderService;
        this.paymentRequestBuilder = paymentRequestBuilder;
        this.authenticationFacade = authenticationFacade;
    }

    public ThreedsInitialize payment(CreatePayRequest req) {
        try {
            String orderNumber = "ORD-" + UUID.randomUUID()
                    .toString()
                    .substring(0, 6)
                    .toUpperCase();


            BigDecimal totalPrice = req.getBasketItems().stream()
                    .map(bi -> {
                        Attribute productAttribute = attributeService.getAttributeById(bi.getAttributeId());
                        BigDecimal price = productAttribute.getDiscountPrice().compareTo(BigDecimal.ZERO) > 0 ?
                                productAttribute.getDiscountPrice() : productAttribute.getPrice();
                        BigDecimal quantity = BigDecimal.valueOf(bi.getQuantity());
                        return price.multiply(quantity);
                    }).reduce(BigDecimal.ZERO, BigDecimal::add);
            Order order = orderService.createOrder(
                    orderNumber,
                    req,
                    totalPrice,
                    PaymentType.CREDIT_CARD,
                    PaymentStatus.PENDING
            );

            CreatePaymentRequest paymentRequest = paymentRequestBuilder.build(
                    req,
                    order,
                    order.getOrderNo(),
                    paymentCallbackUrl,
                    req.getCartId()
            );

            return ThreedsInitialize.create(paymentRequest,options);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }


    public ThreedsPayment completePayment(String paymentId, String conversationId) {
        try {
            CreateThreedsPaymentRequest threedsPaymentRequest = new CreateThreedsPaymentRequest();
            threedsPaymentRequest.setPaymentId(paymentId);
            threedsPaymentRequest.setConversationId(conversationId);
            threedsPaymentRequest.setLocale(Locale.TR.getValue());

            return ThreedsPayment.create(threedsPaymentRequest, options);

        } catch (RuntimeException e) {
            throw new RuntimeException("Sistemde yaşanan bir sorundan dolayı ödeme alınamadı lütfen tekrar deneyiniz.");
        }
    }


    public PaymentResult handlePayment(String paymentId, String conversationId) {
        if (paymentId == null || conversationId == null) {
            throw new PaymentException("Beklenmedik bir hata oluştu lütfen tekrar deneyiniz.");
        }

        ThreedsPayment threedsPayment = completePayment(paymentId, conversationId);

        if ("success".equals(threedsPayment.getStatus())) {
            Order order = orderService.getOrderByOrderNo(conversationId);
            for (SellerOrder so : order.getSellerOrders()) {
                sellerOrderService.completeOrder(so.getId().toString(), PaymentStatus.PAID);
            }
            return PaymentResult.SUCCESS;
        } else {
            return PaymentResult.FAILURE;
        }
    }

}
