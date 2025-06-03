package com.nexaplatform.api.controllers.services.mappers;

import com.nexaplatform.api.controllers.services.dto.in.RoleDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.RoleDtoOut;
import com.nexaplatform.domain.models.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RoleDtoMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    RoleDtoOut toDtoOut(Role role);

    @Mapping(target = "name", qualifiedByName = "toUpperCase")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "active", source = "active")
    Role toDomain(RoleDtoIn dtoIn);

    @Named("toUpperCase")
    default String toUpperCase(String value) {
        return value != null ? value.toUpperCase(): null;
    }

    List<RoleDtoOut> toDtoOutList(List<Role> roles);
}
