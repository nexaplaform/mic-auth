package com.nexaplatform.domain.errors;

import lombok.Getter;

@Getter
public enum Error {

    USER_NOT_FOUND("EL usuario con el identificador %s no existe");

    private final String value;

    Error(String value) {
        this.value = value;
    }
}
