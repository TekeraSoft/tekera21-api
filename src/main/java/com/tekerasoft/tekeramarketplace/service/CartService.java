package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.AddToCartRequest;
import com.tekerasoft.tekeramarketplace.model.entity.Cart;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CartRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    @Value("${app.guest-cart-ttl-days}")
    private long guestCartTtlDays;

    private final CartRepository cartRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    public CartService(CartRepository cartRepository, RedisTemplate<String, Object> redisTemplate) {
        this.cartRepository = cartRepository;
        this.redisTemplate = redisTemplate;
    }

}
