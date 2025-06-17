package com.tekerasoft.tekeramarketplace.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins(
//                        "https://ar.tekera21.com",
//                        "https://tekera21.com",
//                        "https://arzuamber.com"
//                )
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("*")
                .allowCredentials(true); // Bu satır kritik: Cookie/token gibi verilerin taşınmasına izin verir
    }
}
