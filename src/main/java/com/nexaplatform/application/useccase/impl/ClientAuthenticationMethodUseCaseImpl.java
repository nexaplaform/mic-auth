package com.nexaplatform.application.useccase.impl;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplatform.application.useccase.ClientAuthenticationMethodUseCase;
import com.nexaplatform.domain.models.AuthenticationMethod;
import com.nexaplatform.domain.repository.AuthenticationMethodRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClientAuthenticationMethodUseCaseImpl implements ClientAuthenticationMethodUseCase {

    private final AuthenticationMethodRepository repository;

    @Override
    public AuthenticationMethod create(AuthenticationMethod entity) {
        return repository.create(entity);
    }

    @Override
    public List<AuthenticationMethod> getPaginated(Integer page, Integer size, SortEnumDTO sort) {
        return repository.getPaginated(page, size, sort);
    }

    @Override
    public AuthenticationMethod getById(Long id) {
        return repository.getById(id);
    }

    @Override
    public AuthenticationMethod update(Long id, AuthenticationMethod entity) {
        return repository.update(id, entity);
    }

    @Override
    public void delete(Long id) {
        repository.delete(id);
    }
}
