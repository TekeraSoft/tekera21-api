package com.tekerasoft.tekeramarketplace.config;

import com.iyzipay.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Value("${spring.iyzico.api_key}")
    private String apiKey;
    @Value("${spring.iyzico.secret_key}")
    private String secretKey;
    @Value("${spring.iyzico.base_url}")
    private String baseUrl;

    @Bean
    public Options paymentOptions() {
        Options options = new Options();
        options.setApiKey(apiKey);
        options.setSecretKey(secretKey);
        options.setBaseUrl(baseUrl);
        return options;
    }

}
