package com.nexaplatform.application.populator;

import com.nexaplatform.domain.models.Group;
import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.RoleRepository;
import com.nexaplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Log4j2
@Service
@RequiredArgsConstructor
public class GroupHandlerPopulator {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public void populate(Group group) {
        this.populateRoleInGroup(group);
        this.populateUserInGroup(group);
    }

    public void populateRoleInGroup(Group group) {

        if (Objects.nonNull(group.getRoles()) && !group.getRoles().isEmpty()) {
            List<Role> roles = group.getRoles().stream()
                    .map(r -> roleRepository.getById(r.getId())).toList();
            group.setRoles(roles);
            log.debug("Search roles with ids: {}", group.getRoles());
        }
    }

    public void populateUserInGroup(Group group) {

        if (Objects.nonNull(group.getUsers()) && !group.getUsers().isEmpty()) {
            List<User> users = group.getUsers().stream()
                    .map(u -> userRepository.getById(u.getId())).toList();
            group.setUsers(users);
            log.debug("Search users with ids: {}", group.getUsers());
        }
    }
}
