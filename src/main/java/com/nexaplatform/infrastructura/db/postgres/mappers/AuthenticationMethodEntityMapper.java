package com.nexaplatform.infrastructura.db.postgres.mappers;

import com.nexaplatform.domain.models.AuthenticationMethod;
import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthenticationMethodEntityMapper {

    @Mapping(target = "id", ignore = true)
    AuthenticationMethodEntity toEntity(AuthenticationMethod model);

    AuthenticationMethod toDomain(AuthenticationMethodEntity entity);

    List<AuthenticationMethod> toDomainList(List<AuthenticationMethodEntity> entities);
}
