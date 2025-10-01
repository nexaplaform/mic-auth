package com.nexaplatform.infrastructura.db.postgres.mappers;

import com.nexaplatform.domain.models.Group;
import com.nexaplatform.infrastructura.db.postgres.entities.GroupEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GroupEntityMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "uniqueName", source = "uniqueName")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "users", source = "users")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    @Mapping(target = "lastModifiedDate", source = "lastModifiedDate")
    Group toModel(GroupEntity entity);

    List<Group> toModelList(List<GroupEntity> entityList);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "uniqueName", source = "uniqueName")
    @Mapping(target = "active", source = "active")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "users", source = "users")
    GroupEntity toEntity(Group model);
}