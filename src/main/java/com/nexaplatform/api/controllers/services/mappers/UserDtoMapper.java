package com.nexaplatform.api.controllers.services.mappers;

import com.nexaplatform.api.controllers.services.dto.in.UserDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.UserDtoOut;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "fullName", expression = "java(getFullName(user))")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "status", source = "status")
    UserDtoOut toDto(User user);

    default String getFullName(User user) {
        if (user == null) {
            return null;
        }

        String firstName = user.getFirstName() == null ? null : user.getFirstName();
        String lastName = user.getLastName() == null ? null : user.getLastName();
        return firstName + " " + lastName;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", source = "firstName")
    @Mapping(target = "lastName", source = "lastName")
    @Mapping(target = "phoneNumber", source = "phoneNumber")
    @Mapping(target = "email", source = "email")
    @Mapping(target = "password", source = "password")
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRoleIdsToRoles")
    User toDomain(UserDtoIn userDtoIn);

    @Named("mapRoleIdsToRoles")
    default List<Role> mapRoleIdsToRoles(List<Long> rolesIds) {
        if(rolesIds == null){
            return Collections.emptyList();
        }
        return  rolesIds.stream().map( id -> Role.builder()
                .id(id)
                .build()).toList();
    }

    List<UserDtoOut> toDtoList(List<User> user);
}
