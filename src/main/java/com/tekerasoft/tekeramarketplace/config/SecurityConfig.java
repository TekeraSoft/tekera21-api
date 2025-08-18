package com.tekerasoft.tekeramarketplace.config;

import com.tekerasoft.tekeramarketplace.model.enums.Role;
import com.tekerasoft.tekeramarketplace.service.UserService;
import com.tekerasoft.tekeramarketplace.utils.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final Filter filter;

    public SecurityConfig(Filter filter) {
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize // WebSocket izin ver
                        .requestMatchers(
                                "/v1/api/account/**",
                                "/v1/api/product/**",
                                "/v1/api/payment/**",
                                "/v1/api/category/**",
                                "/v1/api/search/**",
                                "/v1/api/digital-fashion/**",
                                "/v1/api/fashion-collection/**",
                                "/v1/api/ws/**",
                                "/ws/**",
                                "/v1/api/auth/**",
                                "/v1/api/user/**",
                                "/v1/api/cart/**"
                        ).permitAll()
                        .requestMatchers("/v1/api/seller/**").hasAnyAuthority(Role.SELLER.name(),Role.SUPER_ADMIN.name())
                        .requestMatchers("/v1/api/seller-support/**").hasAnyAuthority(Role.SELLER_SUPPORT.name(),Role.SUPER_ADMIN.name())
                        .requestMatchers("/v1/api/verification/**").hasAnyAuthority(Role.WITHOUT_APPROVAL_SELLER.name())
                        .requestMatchers("/v1/api/super-admin/**").hasAnyAuthority(Role.SUPER_ADMIN.name())
                        .requestMatchers("/v1/api/user/**").hasAnyAuthority(Role.CUSTOMER.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of(
                "https://frontend.beta.tekera21.com",
                "https://panel.beta.tekera21.com",
                "https://beta.tekera21.com",
                "http://localhost:3000",
                "http://localhost:3002",
                "http://localhost:3001",
                "https://avm.beta.tekera21.com",
                "https://sandbox-api.iyzipay.com",
                "null"
        ));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        // Normal endpointler için CORS kısıtlı
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
