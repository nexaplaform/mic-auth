package com.nexaplatform.infrastructura.db.postgres.repositories;

import com.nexaplatform.infrastructura.db.postgres.entities.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepositoryAdapter extends JpaRepository<RoleEntity, Long> {
}
