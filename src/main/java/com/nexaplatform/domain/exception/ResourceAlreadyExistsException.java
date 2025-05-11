package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResourceAlreadyExistsException extends BaseException {

    public ResourceAlreadyExistsException(String message) {
        super("CT001", message, new ArrayList<>(), ZonedDateTime.now());
    }

    public ResourceAlreadyExistsException(String resourceName, String conflictingValue) {
        super(
                "CT001",
                resourceName + " con valor '" + conflictingValue + "' ya existe.",
                List.of("El valor proporcionado para " + resourceName + " no es único."),
                ZonedDateTime.now()
        );
    }
}
