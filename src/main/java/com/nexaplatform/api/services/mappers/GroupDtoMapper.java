package com.nexaplatform.api.services.mappers;

import com.nexaplatform.api.services.dto.in.GroupDtoIn;
import com.nexaplatform.api.services.dto.out.GroupDtoOut;
import com.nexaplatform.domain.models.Group;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Mapper(componentModel = "spring")
public interface GroupDtoMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "uniqueName", source = "uniqueName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "roles", source = "roles")
    @Mapping(target = "users", source = "users")
    @Mapping(target = "createdBy", source = "createdBy")
    @Mapping(target = "createdDate", source = "createdDate")
    @Mapping(target = "lastModifiedBy", source = "lastModifiedBy")
    @Mapping(target = "lastModifiedDate", source = "lastModifiedDate")
    GroupDtoOut toDto(Group group);

    List<GroupDtoOut> toDtoList(List<Group> groups);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "uniqueName", source = "uniqueName")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "roles", source = "rolesIds", qualifiedByName = "rolesIdsToRoles")
    @Mapping(target = "users", source = "usersIds", qualifiedByName = "usersIdsToUsers")
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdDate", ignore = true)
    @Mapping(target = "lastModifiedBy", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    Group toModel(GroupDtoIn dto);

    @Named("rolesIdsToRoles")
    default List<Role> rolesIdsToRoles(List<Long> rolesIds) {
        if (Objects.isNull(rolesIds) || rolesIds.isEmpty()) {
            return Collections.emptyList();
        }
        return rolesIds.stream().map(id -> Role.builder()
                .id(id)
                .build()).toList();
    }

    @Named("usersIdsToUsers")
    default List<User> usersIdsToUsers(List<Long> usersIds) {
        if (Objects.isNull(usersIds) || usersIds.isEmpty()) {
            return Collections.emptyList();
        }
        return usersIds.stream().map(id -> User.builder()
                .id(id)
                .build()).toList();
    }
}
