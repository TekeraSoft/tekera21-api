package com.tekerasoft.tekeramarketplace.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
               .allowedOrigins(
                       "https://ar.tekera21.com",
                       "https://panel.tekera21.com",
                       "http://localhost:3000",
                       "http://localhost:3001",
                       "http://localhost:3002",
                       "https://tekera21.com",
                       "https://arzuamber.com"
               )
                //.allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("Content-Type","Authorization","token")
                .allowCredentials(true); // Bu satır kritik: Cookie/token gibi verilerin taşınmasına izin verir
    }
}
