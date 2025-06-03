package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplatform.domain.exception.EntityNotFoundException;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.infrastructura.db.postgres.entities.UserEntity;
import com.nexaplatform.infrastructura.db.postgres.mappers.UserEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.UserRepositoryAdapter;
import com.nexaplatform.providers.user.UserProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static com.nexaplatform.domain.errors.Error.USER_NOT_FOUND;
import static com.nexaplatform.providers.user.UserProvider.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRepositoryImplTest {

    @Mock
    private UserEntityMapper uMapper;

    @Mock
    private UserRepositoryAdapter uRepository;

    @InjectMocks
    private UserRepositoryImpl userRepositoryImpl;


    @Test
    void create_ok() {
  
        User inputUser = getUserOne();
        UserEntity entityToSave = getUserEntityOne();
        User createdUser = getUserOne();

        ArgumentCaptor<UserEntity> savedEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);

        when(uMapper.toEntity(inputUser)).thenReturn(entityToSave);
        when(uRepository.save(savedEntityCaptor.capture())).thenReturn(entityToSave);
        when(uMapper.toDomain(any(UserEntity.class))).thenReturn(createdUser);


        User resultUser = userRepositoryImpl.create(inputUser);

        assertNotNull(resultUser);
        assertEquals(createdUser, resultUser);

        verify(uMapper, times(1)).toEntity(inputUser);
        verify(uRepository, times(1)).save(entityToSave);
        verify(uMapper, times(1)).toDomain(savedEntityCaptor.getValue());
    }

    @Test
    void getById_ok() {

        Long userId = 1L;
        UserEntity expectedEntity = getUserEntityOne();
        User expectedUser = getUserOne();

        when(uRepository.findById(userId)).thenReturn(Optional.of(expectedEntity));
        when(uMapper.toDomain(expectedEntity)).thenReturn(expectedUser);

        User actualUser = userRepositoryImpl.getById(userId);

        assertNotNull(actualUser);
        assertEquals(expectedUser, actualUser);

        verify(uMapper).toDomain(expectedEntity);
        verify(uRepository).findById(userId);
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

        Long userId = 1L;
        User inputUser = getUserTwo();
        UserEntity existingEntity = getUserEntityOne();
        User initialRetrievedUser = getUserOne();

        when(uRepository.findById(userId)).thenReturn(Optional.of(existingEntity));
        when(uMapper.toDomain(existingEntity)).thenReturn(initialRetrievedUser);

        User modifiedUserResponse = new User();
        BeanUtils.copyProperties(initialRetrievedUser, modifiedUserResponse);
        BeanUtils.copyProperties(inputUser, modifiedUserResponse);
        modifiedUserResponse.setStatus(initialRetrievedUser.getStatus());

        UserEntity entityToSave = getUserEntityTwo();

        when(uMapper.toEntity(modifiedUserResponse)).thenReturn(entityToSave);

        ArgumentCaptor<UserEntity> savedEntityCaptor = ArgumentCaptor.forClass(UserEntity.class);

        when(uRepository.save(savedEntityCaptor.capture())).thenReturn(entityToSave);
        User finalUpdatedUser = getUserTwo();
        when(uMapper.toDomain(entityToSave)).thenReturn(finalUpdatedUser);

        User actualUser = userRepositoryImpl.update(userId, inputUser);

        assertNotNull(actualUser);
        assertEquals(finalUpdatedUser, actualUser);

        verify(uRepository).findById(userId);
        verify(uMapper).toDomain(existingEntity);

        verify(uMapper).toEntity(modifiedUserResponse);
        verify(uRepository).save(savedEntityCaptor.getValue());
        verify(uMapper).toDomain(savedEntityCaptor.getValue());

        verify(uMapper, times(2)).toDomain(any());
        verify(uRepository, times(1)).save(any());
        verify(uMapper, times(1)).toEntity(any());
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

        Long userId = 1L;
        UserEntity expectedEntity = UserProvider.getUserEntityOne();
        User expectedUser = UserProvider.getUserOne();

        when(uRepository.findById(userId)).thenReturn(Optional.of(expectedEntity));
        when(uMapper.toDomain(expectedEntity)).thenReturn(expectedUser);

        ArgumentCaptor<UserEntity> toDomainArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);

        userRepositoryImpl.delete(userId);

        verify(uRepository, times(1)).findById(userId);
        verify(uRepository, times(1)).deleteById(userId);
        verify(uMapper, times(1)).toDomain(toDomainArgumentCaptor.capture());

        UserEntity actualEntityPassedToToDomain = toDomainArgumentCaptor.getValue();
        assertEquals(expectedEntity, actualEntityPassedToToDomain,
                "La entidad pasada a uMapper.toDomain debe ser igual a la entidad esperada.");
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