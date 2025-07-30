package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.AddToCartRequest;
import com.tekerasoft.tekeramarketplace.model.redisdocument.Cart;
import com.tekerasoft.tekeramarketplace.model.redisdocument.CartItem;
import com.tekerasoft.tekeramarketplace.repository.jparepository.CartRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;

@Service
public class CartService {

    private final ProductService productService;
    @Value("${app.guest-cart-ttl-days}")
    private long guestCartTtlDays;

    private final CartRepository cartRepository;
    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;

    public CartService(CartRepository cartRepository, UserService userService,
                       RedisTemplate<String, Object> redisTemplate, ProductService productService) {
        this.cartRepository = cartRepository;
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.productService = productService;
    }

    private String getCartKey(String userId) {
        return "cart:" + userId;
    }

    public Cart getCart(String userId) {
        Object value = redisTemplate.opsForValue().get(getCartKey(userId));
        return (value instanceof Cart) ? (Cart) value : null;
    }

    public void saveCart(String userId, Cart cart) {
        redisTemplate.opsForValue().set(getCartKey(userId), cart, Duration.ofDays(guestCartTtlDays));
    }

    // Yeni ürün(ler) ekleme
    public void addToCart(String userId, List<AddToCartRequest> req) {
        Cart existingCart = getCart(userId);
        if (existingCart == null) {
            existingCart = productService.toCartItem(req, userId);
        } else {
            Cart newItemsCart = productService.toCartItem(req, userId);
            for (CartItem newItem : newItemsCart.getCartItems()) {
                // Aynı ürün/attribute varsa qty arttır
                CartItem existingItem = existingCart.getCartItems().stream()
                        .filter(ci -> ci.getAttributeId().equals(newItem.getAttributeId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    existingItem.setQuantity(existingItem.getQuantity() + newItem.getQuantity());
                } else {
                    existingCart.getCartItems().add(newItem);
                }
            }
            updateCartTotals(existingCart);
        }
        saveCart(userId, existingCart);
    }

    // Sepetten ürün silme
    public void removeFromCart(String userId, String attributeId) {
        Cart cart = getCart(userId);
        if (cart != null) {
            cart.getCartItems().removeIf(item -> item.getAttributeId().equals(attributeId));
            updateCartTotals(cart);
            saveCart(userId, cart);
        }
    }

    // Sepeti tamamen boşaltma
    public void clearCart(String userId) {
        redisTemplate.delete(getCartKey(userId));
    }

    // Toplam fiyat ve itemCount güncelleme
    private void updateCartTotals(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);
        cart.setItemCount(cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum());
    }
}
