package com.nexaplatform.api.controllers;

import com.nexaplatform.TestContainersConfiguration;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

@Log4j2
@Testcontainers
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import(TestContainersConfiguration.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseIntegration {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    @Transactional
    void setInit() {

        log.info("Antes: Limpiando dinámicamente todas las tablas y reiniciando IDs...");
        List<String> tableNames = jdbcTemplate.queryForList(
                "SELECT tablename FROM pg_tables WHERE schemaname = 'public'",
                String.class
        );

        List<String> tablesToTruncate = tableNames.stream()
                .filter(tableName -> !tableName.startsWith("hibernate_"))
                .toList();

        for (String tableName : tablesToTruncate) {
            try {
                jdbcTemplate.execute("TRUNCATE TABLE " + tableName + " RESTART IDENTITY CASCADE");
            } catch (Exception e) {
                System.err.println("Error al truncar la tabla " + tableName + ": " + e.getMessage());
            }
        }
    }
}
