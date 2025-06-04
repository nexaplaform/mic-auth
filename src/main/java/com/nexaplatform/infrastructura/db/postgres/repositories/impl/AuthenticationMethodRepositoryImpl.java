package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplatform.domain.exception.EntityNotFoundException;
import com.nexaplatform.domain.models.AuthenticationMethod;
import com.nexaplatform.domain.repository.AuthenticationMethodRepository;
import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import com.nexaplatform.infrastructura.db.postgres.mappers.AuthenticationMethodEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.AuthenticationMethodRepositoryAdapter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.nexaplatform.domain.errors.Error.AUTHORIZATION_METHOD_NOT_FOUND;

@Log4j2
@Repository
@RequiredArgsConstructor
public class AuthenticationMethodRepositoryImpl implements AuthenticationMethodRepository {

    private final AuthenticationMethodEntityMapper mapper;
    private final AuthenticationMethodRepositoryAdapter repositoryAdapter;

    @Override
    public AuthenticationMethod create(AuthenticationMethod entity) {
        return mapper.toDomain(repositoryAdapter.save(mapper.toEntity(entity)));
    }

    @Override
    public List<AuthenticationMethod> getPaginated(Integer page, Integer size, Sort.Direction sort) {
        String sortProperty = "id";
        Sort sortObject = Sort.by(sort != null ? sort : Sort.Direction.ASC, sortProperty);
        Pageable pageable = PageRequest.of(page, size, sortObject);
        Page<AuthenticationMethodEntity> roleEntity = repositoryAdapter.findAll(pageable);
        return mapper.toDomainList(roleEntity.getContent());
    }

    @Override
    public AuthenticationMethod getById(Long id) {
        return mapper.toDomain(repositoryAdapter.findById(id).orElseThrow(
                () -> new EntityNotFoundException(AUTHORIZATION_METHOD_NOT_FOUND.getCode(),
                        String.format(AUTHORIZATION_METHOD_NOT_FOUND.getMessage(), id))));
    }

    @Override
    public AuthenticationMethod update(Long id, AuthenticationMethod entity) {
        AuthenticationMethod authenticationMethod = this.getById(id);
        BeanUtils.copyProperties(entity, authenticationMethod, "id");
        entity.setId(id);
        return mapper.toDomain(repositoryAdapter.save(mapper.toEntity(authenticationMethod)));
    }

    @Override
    public void delete(Long id) {
        this.getById(id);
        repositoryAdapter.deleteById(id);
    }
}
