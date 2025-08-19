package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.request.AddToCartRequest;
import com.tekerasoft.tekeramarketplace.model.document.Cart;
import com.tekerasoft.tekeramarketplace.model.document.CartItem;
import com.tekerasoft.tekeramarketplace.repository.mongorepository.CartRepository;
import com.tekerasoft.tekeramarketplace.utils.AuthenticationFacade;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
public class CartService {

    private final ProductService productService;
    private final SettingService settingService;
    private final CartRepository cartRepository;
    private final AuthenticationFacade  authenticationFacade;

    public CartService(ProductService productService, SettingService settingService,
                       CartRepository cartRepository,
                       AuthenticationFacade authenticationFacade) {
        this.settingService = settingService;
        this.cartRepository = cartRepository;
        this.productService = productService;
        this.authenticationFacade = authenticationFacade;
    }

    private Cart getCartFromDb(String cartOwnerId) {
        return cartRepository.findById(cartOwnerId).orElse(null);
    }

    public Cart getCart(String guestUserId) {
        if(guestUserId != null) {
            String currentOwnerId = guestUserId.isEmpty()
                    ? authenticationFacade.getCurrentUserId()
                    : guestUserId;
            return getCartFromDb(currentOwnerId);
        }
        return null;
    }

    public void saveCart(String userId, Cart cart) {
        cart.setId(userId);
        cartRepository.save(cart);
    }

    public Cart addToCart(List<AddToCartRequest> req, String cartId) {
        String userId = authenticationFacade.getCurrentUserId();
        boolean isLoggedIn = userId != null && !userId.isEmpty();

        String cartOwnerId = isLoggedIn ? userId
                : (cartId != null && !cartId.isEmpty() && !"undefined".equals(cartId)
                ? cartId
                : UUID.randomUUID().toString());

        Cart existingCart = getCartFromDb(cartOwnerId);

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
        existingCart.setId(cartOwnerId);

        return existingCart;
    }

    public Cart removeFromCart(String attributeId, String guestUserId) {
        String cartOwnerId = (guestUserId == null || guestUserId.isEmpty())
                ? authenticationFacade.getCurrentUserId()
                : guestUserId;

        Cart cart = getCartFromDb(cartOwnerId);
        if (cart != null) {
            cart.getCartItems().removeIf(item -> item.getAttributeId().equals(attributeId));
            if (cart.getCartItems().isEmpty()) {
                clearCart(cartOwnerId);
            } else {
                updateCartTotals(cart);
                saveCart(cartOwnerId, cart);
            }
        }
        return cart;
    }

    public void mergeGuestCartToUserCart(String guestCartId, String userId) {
        if (guestCartId == null || guestCartId.isEmpty() || userId == null || userId.isEmpty()) {
            return;
        }

        Cart guestCart = getCartFromDb(guestCartId);
        Cart userCart = getCartFromDb(userId);

        if (guestCart == null) {
            return;
        }

        if (userCart == null) {
            guestCart.setId(userId);
            saveCart(userId, guestCart);
        } else {
            for (CartItem guestItem : guestCart.getCartItems()) {
                CartItem existingItem = userCart.getCartItems().stream()
                        .filter(ci -> ci.getAttributeId().equals(guestItem.getAttributeId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    existingItem.setQuantity(guestItem.getQuantity());
                } else {
                    userCart.getCartItems().add(guestItem);
                }
            }
            updateCartTotals(userCart);
            saveCart(userId, userCart);
        }

        cartRepository.deleteById(guestCartId);
    }

    public void clearCart(String cartOwnerId) {
        cartRepository.deleteById(cartOwnerId);
    }

    private void updateCartTotals(Cart cart) {
        BigDecimal totalPrice = cart.getCartItems().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        cart.setTotalPrice(totalPrice);
        cart.setItemCount(cart.getCartItems().stream().mapToInt(CartItem::getQuantity).sum());
        if(totalPrice.compareTo(settingService.getSettings().getMinShippingPrice()) < 0) {
            cart.setShippingPrice(settingService.getSettings().getShippingPrice());
        }
    }
}
