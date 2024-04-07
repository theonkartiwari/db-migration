package com.mastercard.dbmigration;

import com.mastercard.dbmigration.service.DataMigrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DbMigrationApplication implements CommandLineRunner {

	@Autowired
	private DataMigrationService dataMigrationService;

	public static void main(String[] args) {
		SpringApplication.run(DbMigrationApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		dataMigrationService.migrateData();
		System.out.println("Migration completed successfully");
	}
}
