package com.tekerasoft.tekeramarketplace.controller;

import com.tekerasoft.tekeramarketplace.dto.OrderDto;
import com.tekerasoft.tekeramarketplace.dto.ProductUiDto;
import com.tekerasoft.tekeramarketplace.dto.SellerAdminDto;
import com.tekerasoft.tekeramarketplace.dto.SellerOrderDto;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.model.enums.LikeState;
import com.tekerasoft.tekeramarketplace.service.OrderService;
import com.tekerasoft.tekeramarketplace.service.SellerOrderService;
import com.tekerasoft.tekeramarketplace.service.SellerService;
import com.tekerasoft.tekeramarketplace.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/api/user")
public class UserController {

    private final SellerOrderService orderService;
    private final SellerService sellerService;
    private final UserService userService;

    public UserController(SellerOrderService orderService, SellerService sellerService, UserService userService) {
        this.orderService = orderService;
        this.sellerService = sellerService;
        this.userService = userService;
    }

    @GetMapping("/getOrdersByUserId")
    public Page<SellerOrderDto> findOrderByUserId(Pageable pageable) {
        return orderService.findOrderByUserId(pageable);
    }

    @GetMapping("/getSellerInformation")
    public ResponseEntity<SellerAdminDto> getSellerByUserId() {
        return ResponseEntity.ok(sellerService.getSellerInformation());
    }

    @PutMapping("/followSeller")
    public ResponseEntity<ApiResponse<?>> followSeller(@RequestParam String sellerId) {
        return ResponseEntity.ok(userService.followUnfollowSeller(sellerId));
    }

    @PutMapping("/addFavorite")
    public ResponseEntity<ApiResponse<?>> addFavorite(@RequestParam String productId) {
        return ResponseEntity.ok(userService.addToFavoriteProduct(productId));
    }

    @GetMapping("/getFavoriteProducts")
    public ResponseEntity<Page<ProductUiDto>> getFavoriteProducts(Pageable pageable) {
        return ResponseEntity.ok(userService.getFavoriteProducts(pageable));
    }

    @PutMapping("/likeProduct")
    public ResponseEntity<ApiResponse<?>> likeProduct(@RequestParam String productId, @RequestParam LikeState likeState) {
        return ResponseEntity.ok(userService.likeProduct(productId,likeState));
    }

    @GetMapping("/getLikedProducts")
    public ResponseEntity<Page<ProductUiDto>> getLikedProducts(Pageable pageable) {
        return ResponseEntity.ok(userService.getLikedProducts(pageable));
    }

}
