package com.tekerasoft.tekeramarketplace.service;

import com.tekerasoft.tekeramarketplace.dto.SellerAdminDto;
import com.tekerasoft.tekeramarketplace.dto.request.CreateUserRequest;
import com.tekerasoft.tekeramarketplace.dto.request.LoginRequest;
import com.tekerasoft.tekeramarketplace.dto.response.ApiResponse;
import com.tekerasoft.tekeramarketplace.dto.response.JwtResponse;
import com.tekerasoft.tekeramarketplace.exception.UserException;
import com.tekerasoft.tekeramarketplace.model.entity.Seller;
import com.tekerasoft.tekeramarketplace.model.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CartService cartService;
    private final SellerService sellerService;

    public AuthService(UserService userService, AuthenticationManager authenticationManager, JwtService jwtService, CartService cartService, SellerService sellerService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.cartService = cartService;
        this.sellerService = sellerService;
    }

    public JwtResponse authenticate(LoginRequest loginRequest) {
        Optional<User> user = userService.getByUsername(loginRequest.getEmail());
        if (user.isEmpty()) {
            throw new UserException("Email or password is incorrect");
        }

        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
            );
            if (auth.isAuthenticated()) {
                userService.changeLastLoginUser(loginRequest.getEmail());

                // ✅ Kullanıcı id'sini al
                String userId = user.get().getId().toString();

                // ✅ UI'dan gelen guestCartId
                String guestCartId = loginRequest.getCartId();

                // ✅ Sepet birleştirme
                cartService.mergeGuestCartToUserCart(guestCartId, userId);

                // ✅ JWT döndür
                return new JwtResponse(
                        jwtService.generateToken(addClaims(loginRequest.getEmail()), loginRequest.getEmail())
                );
            }
        } catch (Exception e) {
            throw new UserException("Email or password is incorrect");
        }

        throw new UserException("Email or password is incorrect");
    }

    public ApiResponse<?> register(CreateUserRequest req) {
        try {
            Optional<User> user = userService.getByUsername(req.getEmail());
            if(user.isPresent()) {
                throw new UserException("Email already in use");
            }else {
                userService.createUser(req);
                return new ApiResponse<>("User created successfully", HttpStatus.OK.value());
            }
        } catch (RuntimeException e) {
            throw new UserException(e.getMessage());
        }
    }

//    public JwtResponse refreshToken(String refreshToken) {
//        try {
//            // Token'ın geçerliliğini kontrol et
//            if (jwtService.isTokenExpired(refreshToken)) {
//                throw new UserException("Refresh token expired. Please log in again.");
//            }
//
//            // Token geçerli ise, içinden kullanıcı bilgilerini al
//            String email = jwtService.extractUser(refreshToken);
//            Optional<User> user = userService.getByUsername(email);
//
//            if (user.isEmpty()) {
//                throw new UserException("User not found");
//            }
//
//            // Yeni Access Token ve Refresh Token oluştur
//            String newAccessToken = jwtService.generateToken(addClaims(email), email);
//            String newRefreshToken = jwtService.generateRefreshToken(addClaims(email), email);
//
//            return new JwtResponse(newAccessToken);
//        } catch (Exception e) {
//            throw new UserException("Invalid refresh token");
//        }
//    }

    private Map<String, Object> addClaims(String email) {
        Optional<User> user = userService.getByUsername(email);
        Seller seller = sellerService.getSellerByUserId(user.get().getId().toString());
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", user.get().getId());
        if(seller != null) {
            claims.put("sellerId", seller.getId());
        }
        claims.put("roles", user.get().getRoles());
        claims.put("email", user.get().getEmail());
        claims.put("phoneNumber", user.get().getGsmNumber());
        claims.put("nameSurname", user.get().getFirstName()+ " " + user.get().getLastName());
        return claims;
    }
}
