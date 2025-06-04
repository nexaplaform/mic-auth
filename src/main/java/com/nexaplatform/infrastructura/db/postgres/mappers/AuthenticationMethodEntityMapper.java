package com.nexaplatform.infrastructura.db.postgres.mappers;

import com.nexaplatform.domain.models.AuthenticationMethod;
import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuthenticationMethodEntityMapper {

    AuthenticationMethodEntity toEntity(AuthenticationMethod model);

    AuthenticationMethod toDomain(AuthenticationMethodEntity entity);

    List<AuthenticationMethod> toDomainList(List<AuthenticationMethodEntity> entities);
}
