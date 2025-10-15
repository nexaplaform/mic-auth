package com.nexaplatform.infrastructura.db.postgres.mappers;

import com.nexaplatform.domain.models.Role;
import com.nexaplatform.infrastructura.db.postgres.entities.RoleEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface RoleEntityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    Role toDomain(RoleEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    RoleEntity toEntity(Role domain);

    List<Role> toDomainList(List<RoleEntity> entities);

}
