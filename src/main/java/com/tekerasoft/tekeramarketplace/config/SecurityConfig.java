package com.tekerasoft.tekeramarketplace.config;

import com.tekerasoft.tekeramarketplace.model.entity.Role;
import com.tekerasoft.tekeramarketplace.service.UserService;
import com.tekerasoft.tekeramarketplace.utils.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Filter filter;

    public SecurityConfig(UserService userService, PasswordEncoder passwordEncoder, Filter filter) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.filter = filter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(authorize -> authorize // WebSocket izin ver
                        .requestMatchers(
                                "/v1/api/product/**",
                                "/v1/api/category/**",
                                "/v1/api/digital-fashion/**",
                                "/v1/api/fashion-collection/**",
                                "/ws/**",
                                "/v1/api/auth/**"
                        ).permitAll()
                        .requestMatchers("/v1/api/company/**").hasAnyAuthority(Role.COMPANY_ADMIN.name(),Role.SUPER_ADMIN.name())
                        .requestMatchers("/v1/api/super-admin/**").hasAnyAuthority(Role.SUPER_ADMIN.name())
                        .anyRequest().authenticated()
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Sadece güvenilir frontend origin'lerini burada tanımla
        configuration.setAllowedOrigins(List.of(
                "http://localhost:3000",
                "http://localhost:3002",
                "http://localhost:3001",
                "https://avm.tekera21.com"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*")); // İzin verilen header'lar
        configuration.setAllowCredentials(true); // Token, cookie gibi kimlik doğrulama verilerini taşıyabilmek için

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Tüm endpointler için uygula

        return source;
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return  authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
