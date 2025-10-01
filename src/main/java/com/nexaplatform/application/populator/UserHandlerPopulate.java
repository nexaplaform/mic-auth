package com.nexaplatform.application.populator;

import com.nexaplatform.domain.models.Role;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Log4j2
@Component
@RequiredArgsConstructor
public class UserHandlerPopulate {

    private static final String ROLE_USER = "ROLE_USER";
    private static final String ROLE_DESCRIPTION = "Role default for users";


    private final RoleRepository roleRepository;

    public void validateAssociatedRole(User user) {

        if (user.getRoles().isEmpty()) {
            log.info("The user have not associated role: {}", user.getRoles());

            Role existsRoleDefault = roleRepository.findByName(ROLE_USER);
            if (Objects.nonNull(existsRoleDefault)) {
                user.setRoles(List.of(existsRoleDefault));
                return;
            }

            Role savedRole = roleRepository.create(Role.builder()
                    .name(ROLE_USER)
                    .description(ROLE_DESCRIPTION)
                    .active(Boolean.TRUE)
                    .build());

            log.debug("Creation of de default role: {}", savedRole);
            user.setRoles(List.of(savedRole));

        } else {
            List<Role> roles = user.getRoles().stream().map(this::getRole).toList();
            user.setRoles(roles);
        }
    }

    public Role getRole(Role role) {
        return roleRepository.getById(role.getId());
    }
}
