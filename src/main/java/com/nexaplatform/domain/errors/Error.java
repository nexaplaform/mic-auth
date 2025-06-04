package com.nexaplatform.domain.errors;

import lombok.Getter;

import static com.nexaplatform.domain.exception.CodeError.ERROR_CODE_NOT_FOUND;

@Getter
public enum Error {

    USER_NOT_FOUND("EL usuario con el identificador %s no existe", ERROR_CODE_NOT_FOUND),
    AUTHORIZATION_METHOD_NOT_FOUND("EL método de autorizacion con identificador %s no existe", ERROR_CODE_NOT_FOUND),
    ROLE_NOT_FOUND("EL rol con el identificador %s no existe", ERROR_CODE_NOT_FOUND);

    private final String message;
    private final String code;

    Error(String value, String code) {
        this.message = value;
        this.code = code;
    }
}
