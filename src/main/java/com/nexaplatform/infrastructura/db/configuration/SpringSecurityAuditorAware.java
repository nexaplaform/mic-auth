package com.nexaplatform.infrastructura.db.configuration;

import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EnableJpaAuditing
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        //TODO: implementar la logica para sacar el usuario del contexto
        //Cuando este implementada la parte de seguridad
        return Optional.of("Admin");
    }
}
