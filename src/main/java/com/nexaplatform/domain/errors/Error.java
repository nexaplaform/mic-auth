package com.nexaplatform.domain.errors;

import lombok.Getter;

import static com.nexaplatform.domain.exception.CodeError.ERROR_CODE_NOT_FOUND;

@Getter
public enum Error {

    USER_NOT_FOUND(ERROR_CODE_NOT_FOUND, "EL usuario con el identificador %s no existe");

    private final String value;
    private final String code;
    
    Error(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
