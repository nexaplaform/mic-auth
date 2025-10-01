package com.nexaplatform.infrastructura.db.postgres.mappers;

import com.nexaplatform.domain.models.User;
import com.nexaplatform.infrastructura.db.postgres.entities.UserEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserEntityMapper {


    User toDomain(UserEntity entity);

    List<User> toDomainList(List<UserEntity> entity);

    UserEntity toEntity(User user);

}
