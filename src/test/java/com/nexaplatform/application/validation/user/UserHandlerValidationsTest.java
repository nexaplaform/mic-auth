package com.nexaplatform.application.validation.user;

import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.nexaplatform.providers.user.RoleProvider.*;
import static com.nexaplatform.providers.user.UserProvider.getUserOne;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserHandlerValidationsTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserHandlerValidations userHandlerValidations;

    @Test
    void validateAssociatedRole_without_role() {

        User user = getUserOne().withRoles(List.of());

        when(roleRepository.findByName(ROLE_NAME_USER)).thenReturn(getRoleOne());

        userHandlerValidations.validateAssociatedRole(user);

        assertFalse(user.getRoles().isEmpty());
        assertEquals(List.of(getRoleOne()), user.getRoles());

        verify(roleRepository).findByName(ROLE_NAME_USER);
    }

    @Test
    void validateAssociatedRole_when_defaultRole_notExists() {

        User user = getUserOne().withRoles(List.of());

        when(roleRepository.findByName(ROLE_NAME_USER)).thenReturn(null);
        when(roleRepository.create(any(Role.class))).thenReturn(getRoleOne());

        userHandlerValidations.validateAssociatedRole(user);

        assertFalse(user.getRoles().isEmpty());
        assertEquals(List.of(getRoleOne()), user.getRoles());

        verify(roleRepository).findByName(ROLE_NAME_USER);
        verify(roleRepository).create(any(Role.class));
    }

    @Test
    void validateAssociatedRole_with_role() {

        User user = getUserOne();

        when(roleRepository.getById(ROLE_ID_ONE)).thenReturn(getRoleOne());
        when(roleRepository.getById(ROLE_ID_TWO)).thenReturn(getRoleTwo());

        userHandlerValidations.validateAssociatedRole(user);

        assertFalse(user.getRoles().isEmpty());
        assertEquals(List.of(getRoleOne(), getRoleTwo()), user.getRoles());

        verify(roleRepository).getById(ROLE_ID_ONE);
        verify(roleRepository).getById(ROLE_ID_TWO);
    }


    @Test
    void getRole() {

        when(roleRepository.getById(ROLE_ID_ONE)).thenReturn(getRoleOne());

        Role response = userHandlerValidations.getRole(getRoleOne());

        assertNotNull(response);
        assertEquals(getRoleOne(), response);

        verify(roleRepository).getById(ROLE_ID_ONE);
    }
}