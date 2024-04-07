package com.mastercard.dbmigration.config;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
public class MigrationProperties {

    private MigrationConfig migration;

    public MigrationConfig getMigration() {
        return migration;
    }

    public void setMigration(MigrationConfig migration) {
        this.migration = migration;
    }

    public static class MigrationConfig {
        private SourceDatabase source;
        private Map<String, TargetDatabase> targets;

        public SourceDatabase getSource() {
            return source;
        }

        public void setSource(SourceDatabase source) {
            this.source = source;
        }

        public Map<String, TargetDatabase> getTargets() {
            return targets;
        }

        public void setTargets(Map<String, TargetDatabase> targets) {
            this.targets = targets;
        }
    }

    public static class SourceDatabase {
        private String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }
    }

    public static class TargetDatabase {
        private String uri;
        private List<String> collections;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        public List<String> getCollections() {
            return collections;
        }

        public void setCollections(List<String> collections) {
            this.collections = collections;
        }




    }
}
