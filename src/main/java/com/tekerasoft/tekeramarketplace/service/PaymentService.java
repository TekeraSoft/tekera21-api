package com.tekerasoft.tekeramarketplace.service;

import com.iyzipay.Options;
import com.iyzipay.model.Locale;
import com.iyzipay.model.ThreedsInitialize;
import com.iyzipay.model.ThreedsPayment;
import com.iyzipay.request.CreatePaymentRequest;
import com.iyzipay.request.CreateThreedsPaymentRequestV2;
import com.tekerasoft.tekeramarketplace.dto.request.BasketItemRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreateOrderRequest;
import com.tekerasoft.tekeramarketplace.dto.request.CreatePayRequest;
import com.tekerasoft.tekeramarketplace.model.entity.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

@Service
public class PaymentService {

    private final Options options;
    private final OrderService orderService;
    private final UserService userService;
    private final CompanyService companyService;
    private static Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(Options options, OrderService orderService, UserService userService, CompanyService companyService) {
        this.options = options;
        this.orderService = orderService;
        this.userService = userService;
        this.companyService = companyService;
    }

//    public ThreedsInitialize payment(CreatePayRequest req) {
//
//        BigDecimal totalPrice = req.getBasketItems().stream().map(BasketItemRequest::getPrice)
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        Order order = orderService.createOrder(
//                CreateOrderRequest.convertFromPayRequestToOrderRequest(
//                        req,
//                        totalPrice,
//
//                )
//        );
//
//        CreatePaymentRequest paymentRequest = new CreatePaymentRequest();
//        paymentRequest.setLocale(Locale.TR.getValue());
//        paymentRequest.setConversationId(Objects.requireNonNull(order.getId()).toString());
//
//    }

    public ThreedsPayment completePayment(String paymentId, String conversationId) {
        try {
            CreateThreedsPaymentRequestV2 threedsPaymentRequestV2 = new CreateThreedsPaymentRequestV2();
            threedsPaymentRequestV2.setPaymentId(paymentId);
            threedsPaymentRequestV2.setConversationId(conversationId);
            threedsPaymentRequestV2.setLocale(Locale.TR.getValue());

            ThreedsPayment threedsPayment = ThreedsPayment.createV2(threedsPaymentRequestV2, options);

            return threedsPayment;
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            throw new RuntimeException("Error completing 3D Secure Payment");
        }
    }

}
