package com.nexaplatform.api.services.mappers;

import com.nexaplatform.api.services.dto.in.AuthenticationMethodDtoIn;
import com.nexaplatform.api.services.dto.out.AuthenticationMethodDtoOut;
import com.nexaplatform.domain.models.AuthenticationMethod;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthenticationMethodDtoMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "method", source = "method")
    AuthenticationMethod toDomain(AuthenticationMethodDtoIn dto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "method", source = "method")
    AuthenticationMethodDtoOut toDtoOut(AuthenticationMethod model);

    List<AuthenticationMethodDtoOut> toDtoOutList(List<AuthenticationMethod> models);
}
