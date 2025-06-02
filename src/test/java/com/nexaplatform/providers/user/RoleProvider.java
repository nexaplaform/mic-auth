package com.nexaplatform.providers.user;

import com.nexaplatform.api.controllers.services.dto.in.RoleDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.RoleDtoOut;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.infrastructura.db.postgres.entities.RoleEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleProvider {

    public static final Long ROLE_ID_ONE = 1L;
    public static final Long ROLE_ID_TWO = 2L;

    public static final String ROLE_NAME_USER = "USER";
    public static final String ROLE_NAME_ADMIN = "ADMIN";

    public static final String ROLE_DESC_USER = "Role for user generic";
    public static final String ROLE_DESC_ADMIN = "Role for admin app";

    public static final Boolean ROLE_ACTIVE = Boolean.TRUE;

    public static RoleDtoIn getRoleDtoInOne() {
        return RoleDtoIn.builder()
                .name(ROLE_NAME_USER)
                .description(ROLE_DESC_USER)
                .active(ROLE_ACTIVE)
                .build();
    }

    public static RoleDtoIn getRoleDtoInTwo() {
        return RoleDtoIn.builder()
                .name(ROLE_NAME_ADMIN)
                .description(ROLE_DESC_ADMIN)
                .active(ROLE_ACTIVE)
                .build();
    }

    public static RoleDtoOut getRoleDtoOutOne() {
        return RoleDtoOut.builder()
                .id(ROLE_ID_ONE)
                .name(ROLE_NAME_USER)
                .description(ROLE_DESC_USER)
                .active(ROLE_ACTIVE)
                .build();
    }

    public static RoleDtoOut getRoleDtoOutTwo() {
        return RoleDtoOut.builder()
                .id(ROLE_ID_TWO)
                .name(ROLE_NAME_ADMIN)
                .description(ROLE_DESC_ADMIN)
                .active(ROLE_ACTIVE)
                .build();
    }

    public static Role getRoleOne() {
        return Role.builder()
                .id(ROLE_ID_ONE)
                .name(ROLE_NAME_USER)
                .description(ROLE_DESC_USER)
                .active(ROLE_ACTIVE)
                .build();
    }

    public static Role getRoleTwo() {
        return Role.builder()
                .id(ROLE_ID_TWO)
                .name(ROLE_NAME_ADMIN)
                .description(ROLE_DESC_ADMIN)
                .active(ROLE_ACTIVE)
                .build();
    }

    public static RoleEntity getRoleEntityOne() {
        return RoleEntity.builder()
                .name(ROLE_NAME_USER)
                .description(ROLE_DESC_USER)
                .active(ROLE_ACTIVE)
                .users(new ArrayList<>())
                .build();
    }

    public static RoleEntity getRoleEntityTwo() {
        return RoleEntity.builder()
                .name(ROLE_NAME_ADMIN)
                .description(ROLE_DESC_ADMIN)
                .active(ROLE_ACTIVE)
                .users(new ArrayList<>())
                .build();
    }
}
