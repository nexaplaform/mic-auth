package com.nexaplatform.providers.user;

import com.nexaplatform.api.controllers.services.dto.in.UserDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.UserDtoOut;
import com.nexaplatform.domain.models.User;
import com.nexaplatform.domain.models.UserStatus;
import com.nexaplatform.infrastructura.db.postgres.entities.UserEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

import static com.nexaplatform.providers.user.RoleProvider.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class UserProvider {

    public static final String FIRST_NAME_ONE = "John";
    public static final String LAST_NAME_ONE = "Doe";
    public static final String FULL_NAME_ONE = FIRST_NAME_ONE + " " + LAST_NAME_ONE;
    public static final String EMAIL_ONE = "johnDoe@exmaple.com";
    public static final String PASSWORD_ONE = "123456789";
    public static final String PHONE_NUMBER_ONE = "5-5555-5555";
    public static final String FIRST_NAME_TWO = "William";
    public static final String LAST_NAME_TWO = "Thomas";
    public static final String FULL_NAME_TWO = FIRST_NAME_TWO + " " + LAST_NAME_TWO;
    public static final String EMAIL_TWO = "william.t@example.com";
    public static final String PASSWORD_TWO = "mysecret!";
    public static final String PHONE_NUMBER_TWO = "123-555-0103";
    public static final long ID = 1L;

    public static UserDtoIn getUserDtoInOne() {
        return UserDtoIn.builder()
                .firstName(FIRST_NAME_ONE)
                .lastName(LAST_NAME_ONE)
                .email(EMAIL_ONE)
                .password(PASSWORD_ONE)
                .phoneNumber(PHONE_NUMBER_ONE)
                .roles(List.of(1L, 2L))
                .build();
    }

    public static UserDtoIn getUserDtoInTwo() {
        return UserDtoIn.builder()
                .firstName(FIRST_NAME_TWO)
                .lastName(LAST_NAME_TWO)
                .email(EMAIL_TWO)
                .password(PASSWORD_TWO)
                .phoneNumber(PHONE_NUMBER_TWO)
                .roles(List.of(1L, 2L))
                .build();
    }

    public static UserDtoOut getUserDtoOutOne() {
        return UserDtoOut.builder()
                .id(ID)
                .firstName(FIRST_NAME_ONE)
                .lastName(LAST_NAME_ONE)
                .email(EMAIL_ONE)
                .fullName(FULL_NAME_ONE)
                .phoneNumber(PHONE_NUMBER_ONE)
                .status(UserStatus.ACTIVE)
                .roles(List.of(getRoleDtoOutOne(), getRoleDtoOutTwo()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();
    }

    public static UserDtoOut getUserDtoOutTwo() {
        return UserDtoOut.builder()
                .id(2L)
                .firstName(FIRST_NAME_TWO)
                .lastName(LAST_NAME_TWO)
                .email(EMAIL_TWO)
                .fullName(FULL_NAME_TWO)
                .phoneNumber(PHONE_NUMBER_TWO)
                .status(UserStatus.ACTIVE)
                .roles(List.of(getRoleDtoOutOne(), getRoleDtoOutTwo()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();
    }

    public static User getUserOne() {
        return User.builder()
                .id(ID)
                .firstName(FIRST_NAME_ONE)
                .lastName(LAST_NAME_ONE)
                .email(EMAIL_ONE)
                .password(PASSWORD_ONE)
                .phoneNumber(PHONE_NUMBER_ONE)
                .status(UserStatus.ACTIVE)
                .roles(List.of(getRoleOne(), getRoleTwo()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();
    }

    public static User getUserTwo() {
        return User.builder()
                .id(ID)
                .firstName(FIRST_NAME_TWO)
                .lastName(LAST_NAME_TWO)
                .email(EMAIL_TWO)
                .password(PASSWORD_TWO)
                .phoneNumber(PHONE_NUMBER_TWO)
                .status(UserStatus.ACTIVE)
                .roles(List.of(getRoleOne(), getRoleTwo()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();
    }

    public static UserEntity getUserEntityOne() {
        return UserEntity.builder()
                .firstName(FIRST_NAME_ONE)
                .lastName(LAST_NAME_ONE)
                .email(EMAIL_ONE)
                .password(PASSWORD_ONE)
                .phoneNumber(PHONE_NUMBER_ONE)
                .status(UserStatus.ACTIVE)
                .roles(List.of(getRoleEntityOne(), getRoleEntityTwo()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();
    }

    public static UserEntity getUserEntityTwo() {
        return UserEntity.builder()
                .firstName(FIRST_NAME_TWO)
                .lastName(LAST_NAME_TWO)
                .email(EMAIL_TWO)
                .password(PASSWORD_TWO)
                .phoneNumber(PHONE_NUMBER_TWO)
                .status(UserStatus.ACTIVE)
                .roles(List.of(getRoleEntityOne(), getRoleEntityTwo()))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .enabled(true)
                .credentialsNonExpired(true)
                .build();
    }
}
