package com.nexaplatform.application.useccase.impl;

import com.nexaplaform.core.api.dto.SortEnumDTO;
import com.nexaplatform.application.populator.UserHandlerPopulate;
import com.nexaplatform.application.useccase.UserUseCase;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserUseCaseImpl implements UserUseCase, UserDetailsService {

    private final UserRepository uRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserHandlerPopulate userHandlerPopulate;

    @Override
    public User create(User user) {
        userHandlerPopulate.validateAssociatedRole(user);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return uRepository.create(user);
    }

    @Override
    public List<User> getPaginated(Integer page, Integer size, SortEnumDTO sort) {
        return uRepository.getPaginated(page, size, sort);
    }

    @Override
    public User getById(Long id) {
        return uRepository.getById(id);
    }

    @Override
    public User update(Long id, User user) {
        List<Role> roles = user.getRoles().stream().map(userHandlerPopulate::getRole).toList();
        user.setRoles(roles);
        return uRepository.update(id, user);
    }

    @Override
    public void delete(Long id) {
        uRepository.delete(id);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails userDetails = uRepository.findByEmail(username.toLowerCase());
        return userDetails;
    }
}
