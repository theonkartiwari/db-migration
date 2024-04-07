package com.mastercard.dbmigration.service;


import com.mastercard.dbmigration.config.MigrationProperties;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import com.mongodb.client.MongoClients;

import java.util.List;

@Service
public class DataMigrationService {

    private final MongoTemplate sourceMongoTemplate;
    private final MigrationProperties migrationProperties;

    @Autowired
    public DataMigrationService(MongoTemplate sourceMongoTemplate, MigrationProperties migrationProperties) {
        this.sourceMongoTemplate = sourceMongoTemplate;
        this.migrationProperties = migrationProperties;
    }

    public void migrateData() {
        System.out.println("Inside migrate Data");
        MigrationProperties.SourceDatabase sourceDb = migrationProperties.getMigration().getSource();
        MongoTemplate sourceTemplate = new MongoTemplate(MongoClients.create(sourceDb.getUri()), "sourceDB");

        migrationProperties.getMigration().getTargets().forEach((dbKey, targetDb) -> {
            System.out.println("Preparing to migrate to: " + dbKey);
            MongoTemplate targetTemplate = new MongoTemplate(MongoClients.create(targetDb.getUri()), dbKey);
            targetDb.getCollections().forEach(collection -> {
                System.out.println("Migrating collection: " + collection);
                List<Document> documents = sourceTemplate.findAll(Document.class, collection);
                System.out.println("Found " + documents.size() + " documents in collection: " + collection);
                if (!documents.isEmpty()) {
                    targetTemplate.insert(documents, collection);
                    System.out.println("Documents inserted to " + dbKey + " into " + collection);
                } else {
                    System.out.println("No documents found in collection: " + collection);
                }
            });
        });
        System.out.println("Migration completed successfully");
    }

}
