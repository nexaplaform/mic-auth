package com.nexaplatform.api.controllers.services.mappers;

import com.nexaplatform.api.controllers.services.dto.in.AuthenticationMethodDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.AuthenticationMethodDtoOut;
import com.nexaplatform.domain.models.AuthenticationMethod;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthenticationMethodDtoMapper {

    AuthenticationMethod toDomain(AuthenticationMethodDtoIn dto);

    AuthenticationMethodDtoOut toDtoOut(AuthenticationMethod model);

    List<AuthenticationMethodDtoOut> toDtoOutList(List<AuthenticationMethod> models);
}
