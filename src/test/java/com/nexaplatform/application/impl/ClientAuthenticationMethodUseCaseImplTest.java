package com.nexaplatform.application.impl;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplatform.application.useccase.impl.ClientAuthenticationMethodUseCaseImpl;
import com.nexaplatform.domain.models.AuthenticationMethod;
import com.nexaplatform.domain.repository.AuthenticationMethodRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.nexaplatform.providers.authentication.AuthenticationMethodProvider.getAuthenticationMethodOne;
import static com.nexaplatform.providers.authentication.AuthenticationMethodProvider.getAuthenticationMethodTwo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientAuthenticationMethodUseCaseImplTest {

    @Mock
    private AuthenticationMethodRepository repository;
    @InjectMocks
    private ClientAuthenticationMethodUseCaseImpl clientAuthenticationMethodUseCase;

    @Test
    void create() {

        when(repository.create(any(AuthenticationMethod.class))).thenReturn(getAuthenticationMethodOne());

        AuthenticationMethod authenticationMethod = clientAuthenticationMethodUseCase.create(getAuthenticationMethodOne());

        assertNotNull(authenticationMethod);
        assertThat(getAuthenticationMethodOne())
                .usingRecursiveComparison().isEqualTo(authenticationMethod);

        verify(repository).create(any(AuthenticationMethod.class));
    }

    @Test
    void getPaginated() {

        Integer page = 0;
        Integer size = 10;
        SortEnumDTO sort = SortEnumDTO.ASC;

        when(repository.getPaginated(page, size, sort)).thenReturn(List.of(getAuthenticationMethodOne(), getAuthenticationMethodTwo()));

        List<AuthenticationMethod> authenticationMethods = clientAuthenticationMethodUseCase.getPaginated(page, size, sort);

        assertNotNull(authenticationMethods);
        assertEquals(2, authenticationMethods.size());
        assertThat(List.of(getAuthenticationMethodOne(), getAuthenticationMethodTwo()))
                .usingRecursiveComparison()
                .isEqualTo(authenticationMethods);

        verify(repository).getPaginated(page, size, sort);
    }

    @Test
    void getById() {

        when(repository.getById(anyLong())).thenReturn(getAuthenticationMethodOne());

        AuthenticationMethod authenticationMethod = clientAuthenticationMethodUseCase.getById(anyLong());

        assertNotNull(authenticationMethod);
        assertThat(getAuthenticationMethodOne())
                .usingRecursiveComparison().isEqualTo(authenticationMethod);

        verify(repository).getById(anyLong());
    }

    @Test
    void update() {

        when(repository.update(anyLong(), any(AuthenticationMethod.class))).thenReturn(getAuthenticationMethodOne());

        AuthenticationMethod authenticationMethod = clientAuthenticationMethodUseCase.update(1L, getAuthenticationMethodOne());

        assertNotNull(authenticationMethod);
        assertThat(getAuthenticationMethodOne()).usingRecursiveComparison()
                .isEqualTo(authenticationMethod);
    }

    @Test
    void delete() {

        clientAuthenticationMethodUseCase.delete(anyLong());

        verify(repository).delete(anyLong());
    }
}