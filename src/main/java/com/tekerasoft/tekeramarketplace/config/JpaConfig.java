package com.tekerasoft.tekeramarketplace.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.tekerasoft.tekeramarketplace.repository")
@EntityScan(basePackages = "com.tekerasoft.tekeramarketplace.model.entity")
public class JpaConfig {
}
