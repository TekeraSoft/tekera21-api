package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.OrderDto;
import com.tekerasoft.tekeramarketplace.dto.SellerAdminDto;
import com.tekerasoft.tekeramarketplace.dto.SellerOrderDto;
import com.tekerasoft.tekeramarketplace.service.OrderService;
import com.tekerasoft.tekeramarketplace.service.SellerOrderService;
import com.tekerasoft.tekeramarketplace.service.SellerService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    private final SellerOrderService orderService;
    private final SellerService sellerService;

    public UserController(SellerOrderService orderService, SellerService sellerService) {
        this.orderService = orderService;
        this.sellerService = sellerService;
    }

    @GetMapping("/getOrdersByUserId")
    public Page<SellerOrderDto> findOrderByUserId(Pageable pageable) {
        return orderService.findOrderByUserId(pageable);
    }

    @GetMapping("/getSellerInformation")
    public ResponseEntity<SellerAdminDto> getSellerByUserId() {
        return ResponseEntity.ok(sellerService.getSellerInformation());
    }
}
