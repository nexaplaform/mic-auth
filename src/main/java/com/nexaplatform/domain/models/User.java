package com.nexaplatform.domain.models;

import lombok.*;

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
    private UserStatus status;

    public void setInitialStatus() {
        this.status = UserStatus.ACTIVE;
    }
}
