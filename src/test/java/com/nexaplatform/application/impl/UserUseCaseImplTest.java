package com.nexaplatform.application.impl;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplatform.application.populator.UserHandlerPopulate;
import com.nexaplatform.application.useccase.impl.UserUseCaseImpl;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.RoleRepository;
import com.nexaplatform.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

import static com.nexaplatform.providers.user.UserProvider.getUserOne;
import static com.nexaplatform.providers.user.UserProvider.getUserTwo;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserUseCaseImplTest {

    public static final long ID = 1L;
    @Mock
    private UserRepository uRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserHandlerPopulate userHandlerPopulate;
    @InjectMocks
    private UserUseCaseImpl userUseCase;

    @Test
    void create() {

        when(uRepository.create(any(User.class))).thenReturn(getUserOne());
        when(passwordEncoder.encode(anyString())).thenReturn("123456789");

        User user = userUseCase.create(getUserOne());

        assertNotNull(user);

        assertThat(getUserOne())
                .usingRecursiveComparison()
                .isEqualTo(user);

        verify(uRepository).create(any(User.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    void getPaginated() {

        int page = 0;
        int size = 10;
        SortEnumDTO sort = SortEnumDTO.ASC;

        when(uRepository.getPaginated(page, size, sort)).thenReturn(List.of(getUserOne(), getUserTwo()));

        List<User> users = userUseCase.getPaginated(page, size, sort);

        assertNotNull(users);
        assertThat(List.of(getUserOne(), getUserTwo()))
                .usingRecursiveComparison()
                .isEqualTo(users);

        verify(uRepository).getPaginated(page, size, sort);
    }

    @Test
    void getById() {

        when(uRepository.getById(ID)).thenReturn(getUserOne());

        User user = userUseCase.getById(ID);

        assertNotNull(user);
        assertThat(getUserOne())
                .usingRecursiveComparison()
                .isEqualTo(user);

        verify(uRepository).getById(ID);
    }

    @Test
    void update() {

        when(uRepository.update(anyLong(), any(User.class))).thenReturn(getUserTwo());

        User user = userUseCase.update(ID, getUserTwo().withId(ID));

        assertNotNull(user);
        assertThat(getUserTwo())
                .usingRecursiveComparison()
                .isEqualTo(user);

        verify(uRepository).update(anyLong(), any(User.class));
    }

    @Test
    void delete() {

        userUseCase.delete(ID);

        verify(uRepository).delete(ID);
    }
}