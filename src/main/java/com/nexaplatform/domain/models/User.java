package com.nexaplatform.domain.models;

import lombok.*;

import java.util.List;

@Data
@With
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String password;
    @Builder.Default
    private UserStatus status = UserStatus.ACTIVE;
    private List<Role> roles;
}
