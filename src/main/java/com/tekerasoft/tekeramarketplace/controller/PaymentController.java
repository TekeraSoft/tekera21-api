package com.tekerasoft.tekeramarketplace.controller;

import com.iyzipay.model.ThreedsInitialize;
import com.tekerasoft.tekeramarketplace.dto.request.CreatePayRequest;
import com.tekerasoft.tekeramarketplace.model.enums.PaymentResult;
import com.tekerasoft.tekeramarketplace.service.CartService;
import com.tekerasoft.tekeramarketplace.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/v1/api/payment")
public class PaymentController {

    private final CartService cartService;
    @Value("${spring.origin.url}")
    private String originUrl;

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService, CartService cartService) {
        this.paymentService = paymentService;
        this.cartService = cartService;
    }

    @PostMapping("/pay")
    public ResponseEntity<ThreedsInitialize> pay(@RequestBody CreatePayRequest req) {
        return ResponseEntity.ok(paymentService.payment(req));
    }

    @PostMapping("/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String paymentId = request.getParameter("paymentId");
        String conversationId = request.getParameter("conversationId");
        String cartId = request.getParameter("_cid");

        System.out.println("cartId: " + cartId);

        PaymentResult result = paymentService.handlePayment(paymentId, conversationId);
        cartService.clearCart(cartId);

        if (result == PaymentResult.SUCCESS) {
            response.sendRedirect(originUrl+"/odeme/basarili");
        } else {
            response.sendRedirect(originUrl+"/odeme/basarisiz");
        }
    }
}
