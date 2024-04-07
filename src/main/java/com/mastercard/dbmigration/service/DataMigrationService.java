package com.mastercard.dbmigration.service;

import com.mastercard.dbmigration.config.MongoConfig;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataMigrationService {

    private final MongoTemplate sourceMongoTemplate;
    private final MongoConfig mongoConfig;
    private final Environment env;


    @Autowired
    public DataMigrationService(MongoTemplate sourceMongoTemplate, MongoConfig mongoConfig, Environment env) {
        this.sourceMongoTemplate = sourceMongoTemplate;
        this.mongoConfig = mongoConfig;
        this.env = env;
    }

    public void migrateData() throws Exception {
        List<String> targetDbs = List.of("db1", "db2", "db3"); // Add more DB identifiers as needed
        List<String> collectionsToCopy = List.of("collection1", "collection2"); // Specify collections

        for (String collection : collectionsToCopy) {
            List<Document> documents = sourceMongoTemplate.findAll(Document.class, collection);
            for (String db : targetDbs) {
                String targetDbProperty = "spring.data.mongodb.target." + db + ".uri";
                MongoTemplate targetTemplate = mongoConfig.createMongoTemplateForTarget(targetDbProperty);
                if (!documents.isEmpty()) {
                    targetTemplate.insert(documents, collection);
                }
            }
        }
    }
}

