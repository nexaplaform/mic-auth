package com.nexaplatform.providers.user;


import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.models.UserStatus;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class UserProvider {

    public static final String FIRST_NAME = "John";
    public static final String LAST_NAME = "Doe";
    public static final String FULL_NAME= FIRST_NAME + " " + LAST_NAME;
    public static final String EMAIL = "johnDoe@exmaple.com";
    public static final String PASSWORD = "123456789";
    public static final String PHONE_NUMBER = "5 5555 5555";

    public static User getUser() {
        return User.builder()
                .id(1L)
                .firstName(FIRST_NAME)
                .lastName(LAST_NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .phoneNumber(PHONE_NUMBER)
                .status(UserStatus.INACTIVE)
                .build();
    }
}
