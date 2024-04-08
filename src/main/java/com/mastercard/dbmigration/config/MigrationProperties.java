package com.mastercard.dbmigration.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import java.util.Map;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "app")
@Data
public class MigrationProperties {

    private MigrationConfig migration;

    @Data
   public static class MigrationConfig {
        private SourceDatabase source;
        private Map<String, TargetDatabase> targets;
    }

   @Data
   public static class SourceDatabase {
        private String uri;
    }

    @Data
    public static class TargetDatabase {
        private String uri;
        private List<String> collections;
    }
}
