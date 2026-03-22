package com.insurai.backend;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;
import org.springframework.jdbc.core.JdbcTemplate;

import java.nio.charset.StandardCharsets;
import java.io.File;
import java.nio.file.Files;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final JdbcTemplate jdbcTemplate;

    public DatabaseSeeder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(String... args) throws Exception {
        try {
            File seedFile = ResourceUtils.getFile("file:../database_seed.sql");
            if (seedFile.exists()) {
                String sql = new String(Files.readAllBytes(seedFile.toPath()), StandardCharsets.UTF_8);
                jdbcTemplate.execute(sql);
                System.out.println("Database seeded successfully!");
            } else {
                System.out.println("Warning: database_seed.sql not found at ../database_seed.sql");
            }
        } catch (Exception e) {
            System.err.println("Could not run database seed script: " + e.getMessage());
        }
    }
}
