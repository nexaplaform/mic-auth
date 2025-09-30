package com.nexaplatform.api.services.dto.in;

import lombok.Getter;

public enum AuthenticationMethodEnum {

    CLIENT_SECRET_BASIC("client_secret_basic"),
    CLIENT_SECRET_POST("client_secret_post"),
    CLIENT_SECRET_JWT("client_secret_jwt"),
    PRIVATE_KEY_JWT("private_key_jwt"),
    NONE("none"),
    TLS_CLIENT_AUTH("tls_client_auth"),
    SELF_SIGNED_TLS_CLIENT_AUTH("self_signed_tls_client_auth");

    @Getter
    private String value;

    AuthenticationMethodEnum(String value) {
        this.value = value;
    }
}
