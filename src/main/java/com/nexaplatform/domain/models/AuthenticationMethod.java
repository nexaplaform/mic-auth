package com.nexaplatform.domain.models;

import lombok.*;

@Data
@With
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationMethod {

    private Long id;
    private String method;
}
