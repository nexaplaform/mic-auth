package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplatform.domain.models.AuthenticationMethod;
import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import com.nexaplatform.infrastructura.db.postgres.mappers.AuthenticationMethodEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.AuthenticationMethodRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static com.nexaplatform.providers.authentication.AuthenticationMethodProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationMethodRepositoryImplTest {

    @Mock
    private AuthenticationMethodEntityMapper mapper;
    @Mock
    private AuthenticationMethodRepositoryAdapter repositoryAdapter;
    @InjectMocks
    private AuthenticationMethodRepositoryImpl authenticationMethodRepository;

    @Test
    void create() {

        when(mapper.toDomain(any(AuthenticationMethodEntity.class))).thenReturn(getAuthenticationMethodOne());
        when(mapper.toEntity(any(AuthenticationMethod.class))).thenReturn(getAuthenticationMethodEntityOne());
        when(repositoryAdapter.save(any(AuthenticationMethodEntity.class))).thenReturn(getAuthenticationMethodEntityOne());

        AuthenticationMethod authenticationMethod = authenticationMethodRepository.create(getAuthenticationMethodOne());

        assertNotNull(authenticationMethod);
        assertThat(getAuthenticationMethodOne())
                .usingRecursiveComparison()
                .isEqualTo(authenticationMethod);

        verify(mapper).toDomain(any(AuthenticationMethodEntity.class));
        verify(mapper).toEntity(any(AuthenticationMethod.class));
        verify(repositoryAdapter).save(any(AuthenticationMethodEntity.class));
    }

    @Test
    void getPaginated() {


        int page = 0;
        int size = 10;
        String sortProperty = "id";
        Sort.Direction sort = Sort.Direction.ASC;
        Sort sortObject = Sort.by(sort, sortProperty);

        Pageable expectedPageable = PageRequest.of(page, size, sortObject);
        List<AuthenticationMethodEntity> mokRoleEntity = List.of(getAuthenticationMethodEntityOne(), getAuthenticationMethodEntityTwo());
        Page<AuthenticationMethodEntity> mockRoleEntityPage = new PageImpl<>(mokRoleEntity, expectedPageable, 2);

        when(repositoryAdapter.findAll(expectedPageable)).thenReturn(mockRoleEntityPage);
        when(mapper.toDomainList(mokRoleEntity)).thenReturn(List.of(getAuthenticationMethodOne(), getAuthenticationMethodTwo()));

        List<AuthenticationMethod> authenticationMethods = authenticationMethodRepository.getPaginated(page, size, sort);

        assertNotNull(authenticationMethods);
        assertEquals(2, authenticationMethods.size());

        assertThat(List.of(getAuthenticationMethodOne(), getAuthenticationMethodTwo()))
                .usingRecursiveComparison()
                .isEqualTo(authenticationMethods);
    }

    @Test
    void getById() {

        when(mapper.toDomain(any(AuthenticationMethodEntity.class))).thenReturn(getAuthenticationMethodOne());
        when(repositoryAdapter.findById(anyLong())).thenReturn(Optional.of(getAuthenticationMethodEntityOne()));

        AuthenticationMethod authenticationMethod = authenticationMethodRepository.getById(anyLong());

        assertNotNull(authenticationMethod);

        assertThat(getAuthenticationMethodOne())
                .usingRecursiveComparison()
                .isEqualTo(authenticationMethod);

        verify(mapper).toDomain(any(AuthenticationMethodEntity.class));
        verify(repositoryAdapter).findById(anyLong());
    }

    @Test
    void update() {

        when(repositoryAdapter.findById(anyLong())).thenReturn(Optional.of(getAuthenticationMethodEntityOne()));
        when(mapper.toDomain(any(AuthenticationMethodEntity.class))).thenReturn(getAuthenticationMethodOne());

        when(repositoryAdapter.save(any(AuthenticationMethodEntity.class))).thenReturn(getAuthenticationMethodEntityTwo());
        when(mapper.toEntity(any(AuthenticationMethod.class))).thenReturn(getAuthenticationMethodEntityTwo());

        AuthenticationMethod authenticationMethod = authenticationMethodRepository.update(anyLong(), getAuthenticationMethodTwo());

        assertNotNull(authenticationMethod);
        assertThat(getAuthenticationMethodTwo().withId(1L))
                .usingRecursiveComparison()
                .isEqualTo(authenticationMethod);

    }

    @Test
    void delete() {

        when(mapper.toDomain(any(AuthenticationMethodEntity.class))).thenReturn(getAuthenticationMethodOne());
        when(repositoryAdapter.findById(anyLong())).thenReturn(Optional.of(getAuthenticationMethodEntityOne()));

        authenticationMethodRepository.delete(anyLong());

        verify(mapper).toDomain(any(AuthenticationMethodEntity.class));
        verify(repositoryAdapter).findById(anyLong());
    }
}