package com.nexaplatform.application.impl;

import com.nexaplatform.application.UserUseCase;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.RoleRepository;
import com.nexaplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserUseCaseImpl implements UserUseCase, UserDetailsService {

    private final UserRepository uRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User create(User user) {
        List<Role> roles = user.getRoles().stream().map(this::getRole).toList();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(roles);
        return uRepository.create(user);
    }

    private Role getRole(Role r) {
        return roleRepository.getById(r.getId());
    }

    @Override
    public List<User> getPaginated(Integer page, Integer size, Sort.Direction sort) {
        return uRepository.getPaginated(page, size, sort);
    }

    @Override
    public User getById(Long id) {
        return uRepository.getById(id);
    }

    @Override
    public User update(Long id, User user) {
        List<Role> roles = user.getRoles().stream().map(this::getRole).toList();
        user.setRoles(roles);
        return uRepository.update(id, user);
    }

    @Override
    public void delete(Long id) {
        uRepository.delete(id);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return uRepository.findByEmail(username);
    }
}
