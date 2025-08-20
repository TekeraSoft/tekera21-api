package com.tekerasoft.tekeramarketplace.utils;

import com.tekerasoft.tekeramarketplace.service.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * JWT (JSON Web Token) kimlik doğrulama filtresi.
 * Her HTTP isteği için bir kez çalışır ve gelen istekteki çerezlerden JWT'yi çıkarır,
 * doğrular ve kullanıcıyı Spring Security bağlamına (SecurityContextHolder) yerleştirir.
 */
@Component
public class Filter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(Filter.class);

    private final JwtService jwtService;
    private final UserDetailsService userService; // UserDetailsService kullanmak daha genel ve esnektir

    /**
     * JwtAuthFilter'ın bağımlılıklarını enjekte eden yapıcı metot.
     *
     * @param jwtService JWT token işlemleri için servis
     * @param userService Kullanıcı detaylarını yüklemek için servis (UserDetailsService implementasyonu)
     */

    public Filter(JwtService jwtService, UserDetailsService userService) {
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            @Nonnull HttpServletRequest request,
            @Nonnull HttpServletResponse response,
            @Nonnull FilterChain filterChain) throws ServletException, IOException {

        // 1. Çerezlerden JWT token'ı çıkar
        String token = extractToken(request);

        // Eğer token yoksa, filtre zincirine devam et
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String username = null;
        try {
            // 2. Token'dan kullanıcı adını çıkar
            username = jwtService.extractUser(token);
            // 3. Kullanıcı adı mevcutsa ve mevcut bir kimlik doğrulama yoksa
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                // 4. Kullanıcı detaylarını yükle
                UserDetails userDetails = userService.loadUserByUsername(username);
                // 5. Token'ı doğrula
                if (jwtService.validateToken(token, userDetails)) {
                    // 6. Kimlik doğrulama nesnesi oluştur ve güvenlik bağlamına ayarla
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    // Geçersiz token durumunda da çerezleri temizle
                    clearAuthCookies(response);
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.getWriter().write("{\"error\": \"Invalid or expired token. Please log in again.\"}");
                    response.setContentType("application/json");
                    response.getWriter().flush();
                    return;
                }
            }
        } catch (ExpiredJwtException e) {
            clearAuthCookies(response);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"error\": \"Session expired. Please log in again.\"}");
            response.setContentType("application/json");
            response.getWriter().flush();
            return; // Filtre zincirini burada kes
        } catch (Exception e) {
            clearAuthCookies(response); // Hata durumunda da çerezleri temizle
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Authentication failed due to an invalid request.\"}");
            response.setContentType("application/json");
            response.getWriter().flush();
            return; // Filtre zincirini burada kes
        }

        // 9. Filtre zincirine devam et
        filterChain.doFilter(request, response);
    }


    public static String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7); // "Bearer " sonrası
        }
        return null;
    }


    private void clearAuthCookies(HttpServletResponse response) {
        // session-token çerezini sil
        Cookie sessionCookie = new Cookie("token", null);
        sessionCookie.setPath("/");
        sessionCookie.setHttpOnly(true);
        sessionCookie.setMaxAge(0); // Çerezi hemen sil
        response.addCookie(sessionCookie);
    }
}
