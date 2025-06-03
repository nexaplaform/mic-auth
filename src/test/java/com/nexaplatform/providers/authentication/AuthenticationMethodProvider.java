package com.nexaplatform.providers.authentication;

import com.nexaplatform.api.controllers.services.dto.in.AuthenticationMethodDtoIn;
import com.nexaplatform.api.controllers.services.dto.out.AuthenticationMethodDtoOut;
import com.nexaplatform.domain.models.AuthenticationMethod;
import com.nexaplatform.infrastructura.db.postgres.entities.AuthenticationMethodEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class AuthenticationMethodProvider {

    public static AuthenticationMethodDtoIn getAuthenticationMethodDtoInOne() {
        return AuthenticationMethodDtoIn.builder()
                .method("client_secret_basic")
                .build();
    }

    public static AuthenticationMethodDtoIn getAuthenticationMethodDtoInTwo() {
        return AuthenticationMethodDtoIn.builder()
                .method("client_secret_post")
                .build();
    }

    public static AuthenticationMethod getAuthenticationMethodOne() {
        return AuthenticationMethod.builder()
                .id(1L)
                .method("client_secret_basic")
                .build();
    }

    public static AuthenticationMethod getAuthenticationMethodTwo() {
        return AuthenticationMethod.builder()
                .id(2L)
                .method("client_secret_post")
                .build();
    }

    public static AuthenticationMethodEntity getAuthenticationMethodEntityOne() {
        return AuthenticationMethodEntity.builder()
                .method("client_secret_basic")
                .build();
    }

    public static AuthenticationMethodEntity getAuthenticationMethodEntityTwo() {
        return AuthenticationMethodEntity.builder()
                .method("client_secret_post")
                .build();
    }

    public static AuthenticationMethodDtoOut getAuthenticationMethodDtoOutOne() {
        return AuthenticationMethodDtoOut.builder()
                .id(1L)
                .method("client_secret_basic")
                .build();
    }

    public static AuthenticationMethodDtoOut getAuthenticationMethodDtoOutTwo() {
        return AuthenticationMethodDtoOut.builder()
                .id(2L)
                .method("client_secret_post")
                .build();
    }
}
