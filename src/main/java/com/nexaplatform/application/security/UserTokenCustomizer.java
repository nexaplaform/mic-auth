package com.nexaplatform.application.security;

import com.nexaplatform.domain.models.Group;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.GroupRepository;
import com.nexaplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Log4j2
@Configuration
@RequiredArgsConstructor
public class UserTokenCustomizer {

    public static final String ID_TOKEN = "id_token";
    public static final String TOKEN_TYPE = "token_type";

    private final UserRepository userRepository;
    private final GroupRepository groupRepository;

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {

        return context -> {
            Authentication principal = context.getPrincipal();
            if (context.getTokenType().getValue().equals(ID_TOKEN)) {
                context.getClaims().claim(TOKEN_TYPE, "id token");
            }
            if (context.getTokenType().getValue().equals("access_token")) {
                User user = userRepository.findByEmail(principal.getName());
                log.info("Información de usuario: {}", user);
                context.getClaims().claim(TOKEN_TYPE, "access token");
                List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
                List<String> groups = getGetGroups(user.getGroups());
                context.getClaims().claim("id", user.getId());
                context.getClaims().claim("fistName", user.getFirstName());
                context.getClaims().claim("lastName", user.getLastName());
                context.getClaims().claim("fullName", user.getFirstName() + " " + user.getFirstName());
                context.getClaims().claim("roles", roles);
                context.getClaims().claim("status", user.getStatus());
                context.getClaims().claim("groups", groups);
            }
        };
    }

    private List<String> getGetGroups(List<Group> groups) {

        if (Objects.nonNull(groups) && !groups.isEmpty()) {
            return groups.stream()
                    .map(Group::getName)
                    .toList();
        }
        return Collections.emptyList();
    }
}
