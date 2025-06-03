package com.nexaplatform.infrastructura.db.postgres.repositories;

import com.nexaplatform.infrastructura.db.postgres.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepositoryAdapter extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);
}
