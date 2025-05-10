package com.nexaplatform.infrastructura.db.postgres.repositories.impl;

import com.nexaplatform.domain.models.User;
import com.nexaplatform.infrastructura.db.postgres.mappers.UserEntityMapper;
import com.nexaplatform.infrastructura.db.postgres.repositories.UserRepositoryAdapter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static com.nexaplatform.providers.user.UserProvider.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@SpringBootTest
class UserRepositoryImplTest {

    @Mock
    private UserEntityMapper uMapper;

    @Mock
    private UserRepositoryAdapter uRepository;

    @InjectMocks
    private UserRepositoryImpl UserRepositoryImpl;

    @BeforeEach
    void setUp() {
        when(uMapper.toDomain(getUserEntityOne())).thenReturn(getUserOne());
        when(uMapper.toEntity(getUserOne())).thenReturn(getUserEntityOne());
        when(uRepository.save(any())).thenReturn(getUserEntityOne());

        when(uRepository.findById(1L)).thenReturn(Optional.of(getUserEntityOne()));

        when(uRepository.save(getUserEntityTwo())).thenReturn(getUserEntityTwo());
        when(uMapper.toEntity(getUserTwo())).thenReturn(getUserEntityTwo());
        when(uMapper.toDomain(getUserEntityTwo())).thenReturn(getUserTwo());
    }


    @Test
    void create_ok() {

        User user = UserRepositoryImpl.create(getUserOne());

        assertNotNull(user);
        assertEquals(getUserOne(), user);

        verify(uMapper, times(1)).toDomain(any());
        verify(uRepository, times(1)).save(any());
        verify(uMapper, times(1)).toEntity(any());
    }

    @Test
    void getById_ok() {

        User user = UserRepositoryImpl.getById(1L);

        assertNotNull(user);
        assertEquals(getUserOne(), user);

        verify(uMapper).toDomain(any());
        verify(uRepository).findById(1L);
    }

    @Test
    void getById_NotFound() {

        User user = UserRepositoryImpl.getById(1L);

        assertNotNull(user);
        assertEquals(getUserOne(), user);

        verify(uMapper).toDomain(any());
        verify(uRepository).findById(1L);
    }


    @Test
    void update_ok() {

        User user = UserRepositoryImpl.update(1L, getUserTwo());

        assertNotNull(user);
        assertEquals(getUserTwo(), user);

        verify(uRepository, times(1)).findById(anyLong());
        verify(uMapper, times(2)).toDomain(any());
        verify(uRepository, times(1)).save(any());
    }

    @Test
    void delete_ok() {

        UserRepositoryImpl.delete(1L);

        verify(uRepository, times(1)).findById(1L);
        verify(uRepository, times(1)).deleteById(1L);
        verify(uMapper, times(1)).toDomain(getUserEntityOne());
    }
}