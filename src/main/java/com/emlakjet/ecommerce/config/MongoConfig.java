package com.emlakjet.ecommerce.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class MongoConfig {

    @Autowired
    private Environment environment;

    @Bean
    public MongoClient mongo() {
        var mongoClientSettings = mongoSetting();
        return MongoClients.create(mongoClientSettings);
    }

    @Bean
    public MongoClientSettings mongoSetting() {
        ConnectionString connectionString;
        if (environment.acceptsProfiles(Profiles.of("local"))) {
            log.info("Setting up Mongo connection for local instance");
            connectionString = new ConnectionString("mongodb://localhost:27017/ecommerce");
        } else {
            log.info("Setting up Mongo connection for docker image");
            connectionString = new ConnectionString("mongodb://ecommerce-mongo:27017/ecommerce");
        }

        var settingsBuilder = MongoClientSettings.builder()
                .applyToSocketSettings(builder -> builder
                        .connectTimeout(300, TimeUnit.MILLISECONDS)
                        .readTimeout(1, TimeUnit.SECONDS))
                .applyConnectionString(connectionString)
                .applyToConnectionPoolSettings(builder ->
                        builder.maxSize(10)
                                .minSize(1)
                                .maxConnectionIdleTime(3, TimeUnit.SECONDS)
                                .maxWaitTime(10, TimeUnit.SECONDS))
                .applyToClusterSettings(builder ->
                        builder.serverSelectionTimeout(300, TimeUnit.MILLISECONDS))
                .applicationName("ecommerce");

        return settingsBuilder.build();
    }

    @Bean
    public MongoTemplate mongoTemplate() {
        return new MongoTemplate(mongo(), "ecommerce");
    }
}
