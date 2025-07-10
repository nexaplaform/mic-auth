package com.nexaplatform.infrastructura.db.postgres.repositories;

import com.nexaplatform.infrastructura.db.postgres.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepositoryAdapter extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(String name);
}
