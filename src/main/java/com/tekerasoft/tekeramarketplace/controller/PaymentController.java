package com.tekerasoft.tekeramarketplace.controller;

import com.iyzipay.model.ThreedsInitialize;
import com.tekerasoft.tekeramarketplace.dto.request.CreatePayRequest;
import com.tekerasoft.tekeramarketplace.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay")
    public ResponseEntity<ThreedsInitialize> pay(@RequestBody CreatePayRequest req) {
        return ResponseEntity.ok(paymentService.payment(req));
    }

    @PostMapping("/paymentCheck")
    public ResponseEntity<String> paymentCheck(HttpServletRequest request, HttpServletResponse response) {
        return ResponseEntity.ok(paymentService.paymentCheck(request,response));
    }
}
