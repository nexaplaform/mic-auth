package com.nexaplatform.infrastructura.db.postgres.repositories;

import com.nexaplatform.infrastructura.db.postgres.entities.GroupEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepositoryAdapter extends JpaRepository<GroupEntity, Long> {
}
