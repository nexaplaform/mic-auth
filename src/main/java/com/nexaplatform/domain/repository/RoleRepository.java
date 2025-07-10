package com.nexaplatform.domain.repository;

import com.nexaplatform.domain.models.Role;

public interface RoleRepository extends BaseRepository<Role, Long> {

    Role findByName(String name);
}
