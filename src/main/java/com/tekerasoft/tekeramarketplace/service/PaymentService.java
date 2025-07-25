package com.tekerasoft.tekeramarketplace.service;

import com.iyzipay.Options;
import com.iyzipay.model.*;
import com.iyzipay.model.Buyer;
import com.iyzipay.model.Currency;
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
import java.util.Map;

@Service
public class PaymentService {

    @Value("${spring.origin.url}")
    private String originUrl;

    @Value("${spring.iyzico.callback_url}")
    private String paymentCallbackUrl;

    private final Options options;
    private final OrderService orderService;
    private final UserService userService;
    private final CompanyService companyService;
    private static Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(Options options, OrderService orderService, UserService userService,
                          CompanyService companyService) {
        this.options = options;
        this.orderService = orderService;
        this.userService = userService;
        this.companyService = companyService;
    }

    // Kargo ücreti iki taraf için ayrı gösterilecek

    public ThreedsInitialize payment(CreatePayRequest req) {

        BigDecimal totalPrice = req.getBasketItems().stream().map(BasketItemRequest::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        Order order = orderService.createOrder(
                CreateOrderRequest.convertFromPayRequestToOrderRequest(
                        req,
                        totalPrice,
                        new BigDecimal("100.00"),
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
        buyer.setId(req.getBuyer().getId());
        buyer.setName(req.getBuyer().getName());
        buyer.setSurname(req.getBuyer().getSurname());
        buyer.setGsmNumber(req.getBuyer().getGsmNumber());
        buyer.setEmail(req.getBuyer().getEmail());
        buyer.setIdentityNumber(req.getBuyer().getIdentityNumber());
        buyer.setLastLoginDate(req.getBuyer().getLastLoginDate());
        buyer.setRegistrationDate(req.getBuyer().getRegistrationDate());
        buyer.setRegistrationAddress(req.getBuyer().getRegistrationAddress());
        buyer.setIp(req.getBuyer().getIp());
        buyer.setCity(req.getShippingAddress().getCity());
        buyer.setCountry(req.getShippingAddress().getCountry());
        buyer.setZipCode(req.getShippingAddress().getZipCode());
        paymentRequest.setBuyer(buyer);

        paymentRequest.setCallbackUrl(paymentCallbackUrl);

        paymentRequest.setPrice(totalPrice);
        paymentRequest.setPaidPrice(totalPrice);

    }

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

    public String paymentCheck (HttpServletRequest request, HttpServletResponse response) {
        try {
            Map<String, String[]> parameters =  request.getParameterMap();
            String paymentId = parameters.containsKey("paymentId") ? parameters.get("paymentId")[0] : null;
            String conversationId = parameters.containsKey("conversationId") ? parameters.get("conversationId")[0] : null;

            if(paymentId != null || conversationId != null) {
                throw new PaymentException("Eksik Ödeme Bilgisi");
            }

            ThreedsPayment threedsPayment = completePayment(paymentId, conversationId);

            if("success".equalsIgnoreCase(threedsPayment.getStatus())) {
                response.sendRedirect(originUrl + "/payment-success");
            } else {
                response.sendRedirect(originUrl + "/payment-failure");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new PaymentException("Error completing 3D Secure Payment");
        }
        return null;
    }

}
