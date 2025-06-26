package com.tekerasoft.tekeramarketplace.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.time.Duration;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.tekerasoft.tekeramarketplace.repository.esrepository")
@ComponentScan(basePackages = {"com.tekerasoft.tekeramarketplace"})
public class ElasticConfig {

//    @Value("${spring.elasticsearch.uris}")
//    private String uri;
//
//    @Value("${spring.elasticsearch.username}")
//    private String username;
//
//    @Value("${spring.elasticsearch.password}")
//    private String password;
//
//    @NotNull
//    @Override
//    public ClientConfiguration clientConfiguration() {
//        return ClientConfiguration.builder()
//                .connectedTo(uri)
//                .withBasicAuth(username, password)
//                .build();
//    }
}