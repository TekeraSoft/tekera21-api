package com.tekerasoft.tekeramarketplace.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class AppConfig {

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Sadece güvenilir frontend origin'lerini burada tanımla
        configuration.setAllowedOrigins(List.of(
                "https://ar.tekera21.com",
                "https://tekera21.com",
                "https://arzuamber.com"
        ));

//        configuration.setAllowedOrigins(List.of("*"));

        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(List.of("*")); // İzin verilen header'lar
        configuration.setAllowCredentials(true); // Token, cookie gibi kimlik doğrulama verilerini taşıyabilmek için

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Tüm endpointler için uygula

        return source;
    }

}
