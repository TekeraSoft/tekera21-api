package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.AddToCartRequest;
import com.tekerasoft.tekeramarketplace.model.redisdocument.Cart;
import com.tekerasoft.tekeramarketplace.model.redisdocument.CartItem;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    private final ProductService productService;
    @Value("${app.guest-cart-ttl-days}")
    private long guestCartTtlDays;

    private final UserService userService;
    private final RedisTemplate<String, Object> redisTemplate;
    private final AuthenticationFacade  authenticationFacade;

    public CartService(UserService userService,
                       RedisTemplate<String, Object> redisTemplate, ProductService productService, AuthenticationFacade authenticationFacade) {
        this.userService = userService;
        this.redisTemplate = redisTemplate;
        this.productService = productService;
        this.authenticationFacade = authenticationFacade;
    }

    private String getCartKey(String userId) {
        return "cart:" + userId;
    }

    public Cart getCart(String guestUserId) {
        String currentOwnerId;
        if(guestUserId.isEmpty()){
            currentOwnerId = authenticationFacade.getCurrentUserId();
        }else {
            currentOwnerId = guestUserId;
        }
        Object value = redisTemplate.opsForValue().get(getCartKey(currentOwnerId));
        return (value instanceof Cart) ? (Cart) value : null;
    }

    public void saveCart(String userId, Cart cart) {
        redisTemplate.opsForValue().set(getCartKey(userId), cart, Duration.ofDays(guestCartTtlDays));
    }

    public Cart addToCart(List<AddToCartRequest> req, String cartId) {
        String userId = authenticationFacade.getCurrentUserId();
        boolean isLoggedIn = userId != null && !userId.isEmpty();

        String cartOwnerId;
        if (isLoggedIn) {
            cartOwnerId = userId;
        } else {
            if (cartId != null && !cartId.isEmpty() && !cartId.equals("undefined")) {
                cartOwnerId = cartId;
            } else {
                cartOwnerId = UUID.randomUUID().toString();
            }
        }

        Cart existingCart = getCart(cartOwnerId);

        if (existingCart == null) {
            existingCart = productService.toCartItem(req, cartOwnerId);
        } else {
            Cart newItemsCart = productService.toCartItem(req, cartOwnerId);

            for (CartItem newItem : newItemsCart.getCartItems()) {
                CartItem existingItem = existingCart.getCartItems().stream()
                        .filter(ci -> ci.getAttributeId().equals(newItem.getAttributeId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    existingItem.setQuantity(newItem.getQuantity());
                } else {
                    existingCart.getCartItems().add(newItem);
                }
            }

            updateCartTotals(existingCart);
        }

        saveCart(cartOwnerId, existingCart);

        // 🔹 UI'nin localStorage'a kaydedebilmesi için ID'yi döndür
        existingCart.setId(cartOwnerId);

        return existingCart;
    }

    public Cart removeFromCart(String attributeId,String guestUserId) {
        String cartOwnerId;
        if(guestUserId.isEmpty()){
            cartOwnerId = authenticationFacade.getCurrentUserId();
        } else {
            cartOwnerId = guestUserId;
        }
        Cart cart = getCart(cartOwnerId);
        if (cart != null) {
            if(cart.getCartItems().isEmpty()){
                clearCart();
            }
            cart.getCartItems().removeIf(item -> item.getAttributeId().equals(attributeId));
            updateCartTotals(cart);
            saveCart(cartOwnerId, cart);
        }
        return cart; // Güncel sepeti dön
    }

    public void mergeGuestCartToUserCart(String guestCartId, String userId) {
        if (guestCartId == null || guestCartId.isEmpty() || userId == null || userId.isEmpty()) {
            return;
        }

        Cart guestCart = getCart(guestCartId);
        Cart userCart = getCart(userId);

        if (guestCart == null) {
            return; // Guest sepet yoksa işlem yapmaya gerek yok
        }

        if (userCart == null) {
            // Kullanıcının sepeti yok → guest sepetini direkt userId ile kaydet
            saveCart(userId, guestCart);
        } else {
            // Mevcut user sepeti ile guest sepeti birleştir
            for (CartItem guestItem : guestCart.getCartItems()) {
                CartItem existingItem = userCart.getCartItems().stream()
                        .filter(ci -> ci.getAttributeId().equals(guestItem.getAttributeId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    // UI zaten güncel quantity gönderiyorsa direk set yap
                    existingItem.setQuantity(guestItem.getQuantity());
                } else {
                    userCart.getCartItems().add(guestItem);
                }
            }

            updateCartTotals(userCart);
            saveCart(userId, userCart);
        }

        // ✅ Guest sepeti sil
        redisTemplate.delete(getCartKey(guestCartId));
    }

    public void clearCart() {
        String userId = authenticationFacade.getCurrentUserId();
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
