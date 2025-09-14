package com.personalisedlearn.poseidon.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MongoConfig {
    private static final Logger logger = LoggerFactory.getLogger(MongoConfig.class);

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @PostConstruct
    public void logMongoUri() {
        // Mask the password in the URI for security
        String maskedUri = mongoUri.replaceAll("://(.*?):(.*?)@", "://$1:*****@");
        logger.info("MongoDB URI: {}", maskedUri);
    }
}
