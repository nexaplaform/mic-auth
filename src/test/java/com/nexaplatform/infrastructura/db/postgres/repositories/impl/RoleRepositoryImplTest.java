package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplaform.core.exception.EntityNotFoundException;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.infrastructura.db.postgres.entities.RoleEntity;
import com.nexaplatform.infrastructura.db.postgres.mappers.RoleEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.RoleRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static com.nexaplatform.domain.errors.Error.ROLE_NOT_FOUND;
import static com.nexaplatform.providers.user.RoleProvider.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoleRepositoryImplTest {

    @InjectMocks
    private RoleRepositoryImpl roleRepositoryImpl;
    @Mock
    private RoleEntityMapper mapper;
    @Mock
    private RoleRepositoryAdapter roleRepository;

    @Test
    void create() {
        when(mapper.toEntity(any(Role.class))).thenReturn(getRoleEntityOne());
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(getRoleEntityOne());
        when(mapper.toDomain(any(RoleEntity.class))).thenReturn(getRoleOne());

        Role role = roleRepositoryImpl.create(getRoleOne());

        assertNotNull(role);

        assertThat(getRoleOne())
                .usingRecursiveComparison()
                .isEqualTo(role);
    }

    @Test
    void getPaginated() {

        int page = 0;
        int size = 10;
        String sortProperty = "id";
        SortEnumDTO sort = SortEnumDTO.ASC;
        Sort sortObject = Sort.by(sort.toString(), sortProperty);

        Pageable expectedPageable = PageRequest.of(page, size, sortObject);
        List<RoleEntity> mokRoleEntity = List.of(getRoleEntityOne(), getRoleEntityTwo());
        Page<RoleEntity> mockRoleEntityPage = new PageImpl<>(mokRoleEntity, expectedPageable, 2);

        when(roleRepository.findAll(expectedPageable)).thenReturn(mockRoleEntityPage);
        when(mapper.toDomainList(mokRoleEntity)).thenReturn(List.of(getRoleOne(), getRoleTwo()));

        List<Role> responseData = roleRepositoryImpl.getPaginated(page, size, sort);

        assertNotNull(responseData);
        assertEquals(2, responseData.size());

        assertThat(List.of(getRoleOne(), getRoleTwo()))
                .usingRecursiveComparison()
                .isEqualTo(responseData);
    }

    @Test
    void getById() {
        Long id = 1L;

        when(mapper.toDomain(any(RoleEntity.class))).thenReturn(getRoleOne());
        when(roleRepository.findById(id)).thenReturn(Optional.of(getRoleEntityOne()));

        Role role = roleRepositoryImpl.getById(id);

        assertThat(getRoleOne())
                .usingRecursiveComparison()
                .isEqualTo(role);

        verify(mapper).toDomain(any(RoleEntity.class));
        verify(roleRepository).findById(id);
    }

    @Test
    void get_By_Id_Not_Found() {

        Long id = 100L;

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> roleRepositoryImpl.getById(id)
        );

        assertEquals(ROLE_NOT_FOUND.getCode(), ex.getCode());
        assertEquals(String.format(ROLE_NOT_FOUND.getMessage(), id), ex.getMessage());
    }

    @Test
    void update() {

        Long id = 1L;

        when(mapper.toEntity(any(Role.class))).thenReturn(getRoleEntityOne());
        when(roleRepository.findById(id)).thenReturn(Optional.of(getRoleEntityOne()));
        when(roleRepository.save(any(RoleEntity.class))).thenReturn(getRoleEntityOne());
        when(mapper.toDomain(any(RoleEntity.class))).thenReturn(getRoleOne());

        Role role = roleRepositoryImpl.update(id, getRoleTwo());

        assertNotNull(role);
        assertThat(getRoleTwo().withId(1L))
                .usingRecursiveComparison()
                .isEqualTo(role);
    }

    @Test
    void update_By_Id_Not_Found() {

        Long id = 100L;

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> roleRepositoryImpl.update(id, getRoleTwo())
        );

        assertEquals(ROLE_NOT_FOUND.getCode(), ex.getCode());
        assertEquals(String.format(ROLE_NOT_FOUND.getMessage(), id), ex.getMessage());
    }


    @Test
    void delete() {

        Long id = 1L;

        when(roleRepository.findById(id)).thenReturn(Optional.of(getRoleEntityOne()));
        when(mapper.toDomain(any(RoleEntity.class))).thenReturn(getRoleOne());

        roleRepositoryImpl.delete(id);

        verify(roleRepository).findById(id);
        verify(mapper).toDomain(any(RoleEntity.class));
    }

    @Test
    void delete_Not_Found() {

        Long id = 100L;

        when(roleRepository.findById(id)).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> roleRepositoryImpl.delete(id)
        );

        assertEquals(ROLE_NOT_FOUND.getCode(), ex.getCode());
        assertEquals(String.format(ROLE_NOT_FOUND.getMessage(), id), ex.getMessage());
    }
}