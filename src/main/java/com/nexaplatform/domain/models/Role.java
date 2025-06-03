package com.nexaplatform.domain.models;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

@Data
@With
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Role implements GrantedAuthority {

    private Long id;
    private String name;
    private String description;
    private Boolean active;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
