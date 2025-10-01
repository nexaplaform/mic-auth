package com.nexaplatform.api.services.mappers;

import com.nexaplatform.api.services.dto.in.UserDtoIn;
import com.nexaplatform.api.services.dto.out.UserDtoOut;
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
public interface UserDtoMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "fullName", expression = "java(getFullName(user))")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "status", source = "status")
        //@Mapping(target = "groups", source = "groups")
    UserDtoOut toDto(User user);

    default String getFullName(User user) {
        if (user == null) {
            return null;
        }
        String firstName = Objects.isNull(user.getFirstName()) ? null : user.getFirstName();
        String lastName = Objects.isNull(user.getLastName()) ? null : user.getLastName();
        return firstName + " " + lastName;
    }

    List<UserDtoOut> toDtoList(List<User> user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    @Mapping(target = "groups", source = "groups", qualifiedByName = "mapGroupIdToGroups")
    User toDomain(UserDtoIn userDtoIn);

    @Named("mapRoleIdsToRoles")
    default List<Role> mapRoleIdsToRoles(List<Long> rolesIds) {
        if (rolesIds == null) {
            return Collections.emptyList();
        }
        return rolesIds.stream().map(id -> Role.builder()
                .id(id)
                .build()).toList();
    }

    @Named("mapGroupIdToGroups")
    default List<Group> mapGroupIdToGroups(List<Long> groupIds) {
        if (Objects.isNull(groupIds) || groupIds.isEmpty()) {
            return Collections.emptyList();
        }
        return groupIds.stream().map(id -> Group.builder()
                        .id(id).build())
                .toList();
    }
}
