package com.nexaplatform.application.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

    private Map<String, List<String>> publicUrls;
    private List<String> csrfIgnoredUrls;

    public String[] getAllPublicUrls() {
        return publicUrls.values().stream()
                .flatMap(List::stream)
                .toArray(String[]::new);
    }
}
