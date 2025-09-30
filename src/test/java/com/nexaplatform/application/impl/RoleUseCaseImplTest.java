package com.nexaplatform.application.impl;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplatform.application.useccase.impl.RoleUseCaseImpl;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.nexaplatform.providers.user.RoleProvider.getRoleOne;
import static com.nexaplatform.providers.user.RoleProvider.getRoleTwo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleUseCaseImplTest {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleUseCaseImpl roleUseCase;

    @Test
    void create() {
        when(roleRepository.create(any(Role.class))).thenReturn(getRoleOne());

        Role role = roleUseCase.create(getRoleOne());

        assertNotNull(role);
        assertThat(getRoleOne())
                .usingRecursiveComparison()
                .isEqualTo(role);

        verify(roleRepository).create(any(Role.class));
    }

    @Test
    void getPaginated() {

        int page = 0;
        int size = 10;
        SortEnumDTO sort = SortEnumDTO.ASC;

        when(roleRepository.getPaginated(page, size, sort)).thenReturn(List.of(getRoleOne(), getRoleTwo()));

        List<Role> roles = roleUseCase.getPaginated(page, size, sort);

        assertNotNull(roles);
        assertThat(List.of(getRoleOne(), getRoleTwo()))
                .usingRecursiveComparison()
                .isEqualTo(roles);

        verify(roleRepository).getPaginated(page, size, sort);
    }

    @Test
    void getById() {

        Long id = 1L;
        when(roleRepository.getById(id)).thenReturn(getRoleOne());

        Role role = roleUseCase.getById(id);

        assertNotNull(role);
        assertThat(getRoleOne())
                .usingRecursiveComparison()
                .isEqualTo(role);
    }

    @Test
    void update() {

        Long id = 1L;
        when(roleRepository.update(id, getRoleTwo())).thenReturn(getRoleTwo().withId(id));

        Role role = roleUseCase.update(id, getRoleTwo());

        assertNotNull(role);

        assertThat(getRoleTwo().withId(id))
                .usingRecursiveComparison()
                .isEqualTo(role);
    }

    @Test
    void delete() {

        Long id = 1L;

        roleUseCase.delete(id);

        verify(roleRepository).delete(id);
    }
}