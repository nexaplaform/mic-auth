package com.nexaplatform.infrastructura.db.postgres.repositories;

import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthenticationMethodRepositoryAdapter extends JpaRepository<AuthenticationMethodEntity, Long> {
}
