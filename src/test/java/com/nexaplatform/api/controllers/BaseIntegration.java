package com.nexaplatform.api.controllers;

import com.nexaplatform.TestcontainersConfiguration;
import com.nexaplatform.infrastructura.db.postgres.repositories.UserRepositoryAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@Import(TestcontainersConfiguration.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseIntegration {

    protected static final String BASE_PATH = "v1/users";

    @Autowired
    protected UserRepositoryAdapter uRepository;

    @BeforeEach
    void setInit() {
        uRepository.deleteAll();
    }

    @AfterEach
    void setFinish() {
        uRepository.deleteAll();
    }
}
