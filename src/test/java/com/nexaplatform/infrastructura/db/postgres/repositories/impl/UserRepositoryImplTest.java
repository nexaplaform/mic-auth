package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplatform.domain.exception.EntityNotFoundException;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.infrastructura.db.postgres.entities.UserEntity;
import com.nexaplatform.infrastructura.db.postgres.mappers.UserEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.UserRepositoryAdapter;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static com.nexaplatform.domain.errors.Error.USER_NOT_FOUND;
import static com.nexaplatform.providers.user.UserProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserRepositoryImplTest {

    @Mock
    private UserEntityMapper uMapper;

    @Mock
    private UserRepositoryAdapter uRepository;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;


    @Test
    void create_ok() {

        when(uMapper.toDomain(getUserEntityOne())).thenReturn(getUserOne());
        when(uMapper.toEntity(getUserOne())).thenReturn(getUserEntityOne());
        when(uRepository.save(any())).thenReturn(getUserEntityOne());

        User user = userRepositoryImpl.create(getUserOne());

        assertNotNull(user);
        assertEquals(getUserOne(), user);

        verify(uMapper, times(1)).toDomain(any());
        verify(uRepository, times(1)).save(any());
        verify(uMapper, times(1)).toEntity(any());
    }

    @Test
    void getById_ok() {

        when(uMapper.toDomain(getUserEntityOne())).thenReturn(getUserOne());
        when(uRepository.findById(1L)).thenReturn(Optional.of(getUserEntityOne()));

        User user = userRepositoryImpl.getById(1L);

        assertNotNull(user);
        assertEquals(getUserOne(), user);

        verify(uMapper).toDomain(any());
        verify(uRepository).findById(1L);
    }

    @Test
    void getById_throwsEntityNotFoundException_whenUserNotFound() {

        Long noExistsId = 2L;
        when(uRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userRepositoryImpl.getById(noExistsId)
        );

        assertEquals(USER_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(String.format(USER_NOT_FOUND.getMessage(), noExistsId), exception.getMessage());

        verifyNoInteractions(uMapper);
        verify(uRepository, times(1)).findById(noExistsId);
    }

    @Test
    void update_ok() {

        when(uRepository.findById(1L)).thenReturn(Optional.of(getUserEntityOne()));
        when(uMapper.toDomain(getUserEntityOne())).thenReturn(getUserOne());

        when(uRepository.save(getUserEntityTwo())).thenReturn(getUserEntityTwo());
        when(uMapper.toEntity(getUserTwo())).thenReturn(getUserEntityTwo());
        when(uMapper.toDomain(getUserEntityTwo())).thenReturn(getUserTwo());

        User user = userRepositoryImpl.update(1L, getUserTwo());

        assertNotNull(user);
        assertEquals(getUserTwo(), user);

        verify(uRepository, times(1)).findById(anyLong());
        verify(uMapper, times(2)).toDomain(any());
        verify(uRepository, times(1)).save(any());
    }

    @Test
    void update_throwsEntityNotFoundException_whenUserNotFound() {

        Long noExistsId = 2L;
        when(uRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userRepositoryImpl.update(noExistsId, getUserOne())
        );

        assertEquals(USER_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(String.format(USER_NOT_FOUND.getMessage(), noExistsId), exception.getMessage());

        verify(uRepository, times(1)).findById(noExistsId);
    }

    @Test
    void delete_ok() {

        when(uRepository.findById(1L)).thenReturn(Optional.of(getUserEntityOne()));
        when(uMapper.toDomain(getUserEntityOne())).thenReturn(getUserOne());

        userRepositoryImpl.delete(1L);

        verify(uRepository, times(1)).findById(1L);
        verify(uRepository, times(1)).deleteById(1L);
        verify(uMapper, times(1)).toDomain(getUserEntityOne());
    }

    @Test
    void deleteById_throwsEntityNotFoundException_whenUserNotFound() {

        Long noExistsId = 2L;
        when(uRepository.findById(2L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class,
                () -> userRepositoryImpl.delete(noExistsId)
        );

        assertEquals(USER_NOT_FOUND.getCode(), exception.getCode());
        assertEquals(String.format(USER_NOT_FOUND.getMessage(), noExistsId), exception.getMessage());
    }

    @Test
    void paginated() {

        int page = 0;
        int size = 10;
        String sortProperty = "id";
        Sort.Direction sort = Sort.Direction.DESC;
        Sort sortObject = Sort.by(sort, sortProperty);

        Pageable expectedPageable = PageRequest.of(page, size, sortObject);
        List<UserEntity> mockUserEntities = List.of(getUserEntityOne());
        Page<UserEntity> mockUserEntityPage = new PageImpl<>(mockUserEntities, expectedPageable, 1);

        List<User> expectedUsers = List.of(getUserOne());

        when(uRepository.findAll(expectedPageable)).thenReturn(mockUserEntityPage);
        when(uMapper.toDomainList(mockUserEntities)).thenReturn(expectedUsers);

        List<User> actualUsers = userRepositoryImpl.getPaginated(page, size, sort);

        assertNotNull(actualUsers);
        assertEquals(1, actualUsers.size());
        assertEquals(expectedUsers, actualUsers);

        verify(uRepository).findAll(eq(expectedPageable));
        verify(uMapper).toDomainList(eq(mockUserEntities));
    }
}