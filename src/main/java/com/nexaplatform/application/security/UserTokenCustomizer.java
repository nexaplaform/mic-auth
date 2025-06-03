package com.nexaplatform.application.security;

import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class UserTokenCustomizer {

    public static final String ID_TOKEN = "id_token";

    private final UserRepository userRepository;

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            Authentication principal = context.getPrincipal();
            if (context.getTokenType().getValue().equals(ID_TOKEN)) {
                context.getClaims().claim("token_type", "id token");
            }
            if (context.getTokenType().getValue().equals("access_token")) {
                User user = userRepository.findByEmail(principal.getName());
                context.getClaims().claim("token_type", "access token");
                List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
                context.getClaims().claim("roles", roles);
                context.getClaims().claim("status", user.getStatus());
            }
        };
    }
}
