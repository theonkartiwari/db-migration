package com.mastercard.dbmigration.service;


import com.mastercard.dbmigration.config.MigrationProperties;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.stereotype.Service;
import com.mongodb.client.MongoClients;

import java.util.List;

@Service
@Slf4j
public class DataMigrationService {

    private final MongoTemplate sourceMongoTemplate;
    private final MigrationProperties migrationProperties;

    @Autowired
    public DataMigrationService(MongoTemplate sourceMongoTemplate, MigrationProperties migrationProperties) {
        this.sourceMongoTemplate = sourceMongoTemplate;
        this.migrationProperties = migrationProperties;
    }
;

    public void migrateData() {
        log.info("Starting data migration process.");
        MigrationProperties.SourceDatabase sourceDb = migrationProperties.getMigration().getSource();
        SimpleMongoClientDatabaseFactory sourceFactory = new SimpleMongoClientDatabaseFactory(sourceDb.getUri());
        MongoTemplate sourceTemplate = new MongoTemplate(sourceFactory);

        migrationProperties.getMigration().getTargets().forEach((dbKey, targetDb) -> {
            log.info("Preparing to migrate to database: {}", dbKey);
            SimpleMongoClientDatabaseFactory targetFactory = new SimpleMongoClientDatabaseFactory(targetDb.getUri());
            MongoTemplate targetTemplate = new MongoTemplate(targetFactory);

            targetDb.getCollections().forEach(collection -> {
                log.info("Processing collection: {}", collection);
                List<Document> documents = sourceTemplate.findAll(Document.class, collection);
                if (!documents.isEmpty()) {
                    log.info("Found {} documents in collection: {}. Inserting into target.", documents.size(), collection);
                    documents.forEach(document -> {
                        targetTemplate.save(document, collection);
                    });
                    log.info("Documents processed for collection: {}", collection);
                } else {
                    log.info("No documents found in source collection: {}. No action required.", collection);
                }
            });
        });
        log.info("Data migration completed successfully.");
    }



}
