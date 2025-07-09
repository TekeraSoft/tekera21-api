package com.tekerasoft.tekeramarketplace.config;


import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
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
                       "https://arzuamber.com",
                       "https://www.arzuamber.com"
               )
                //.allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders("Content-Type","Authorization","token")
                .allowCredentials(true); // Bu satır kritik: Cookie/token gibi verilerin taşınmasına izin verir
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(600));
        factory.setMaxRequestSize(DataSize.ofMegabytes(600));
        return factory.createMultipartConfig();
    }
}
