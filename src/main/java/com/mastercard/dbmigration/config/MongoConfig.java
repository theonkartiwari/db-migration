package com.mastercard.dbmigration.config;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongoConfig {

    @Autowired
    private Environment env;

    @Bean
    public MongoTemplate sourceMongoTemplate(){
        String sourceUri = env.getProperty("spring.data.mongodb.source.uri");
        return new MongoTemplate(MongoClients.create(sourceUri),"sourceDB");
    }

    public MongoTemplate createMongoTemplateForTarget(String targetDbProperty) throws Exception {
        String uri = env.getProperty(targetDbProperty);
        MongoClient mongoClient = MongoClients.create(uri);
        String dbName = new ConnectionString(uri).getDatabase(); // Extracts DB name from URI
        return new MongoTemplate(mongoClient, dbName);
    }

}

