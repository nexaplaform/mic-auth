package com.nexaplatform.domain.models.valueobject;

import java.util.Objects;
import java.util.UUID;

public class GenericId {

    private final String value;

    public GenericId(String value) {
        this.value = Objects.requireNonNull(value, "El id no puede ser nulo.") ;
    }

    public static GenericId fron(String value) {
        return new GenericId(value);
    }

    public static GenericId generate() {
        return new GenericId(UUID.randomUUID().toString());
    }


}
