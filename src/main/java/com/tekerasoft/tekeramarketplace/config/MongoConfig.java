package com.tekerasoft.tekeramarketplace.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "com.tekerasoft.tekeramarketplace.repository.nosql")
public class MongoConfig {

}