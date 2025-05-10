package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class ResourceAlreadyExistsException extends BaseException {

    public ResourceAlreadyExistsException(String message) {
        super("CONFLICT-001", message, new ArrayList<>(), ZonedDateTime.now());
    }
    
    public ResourceAlreadyExistsException(String resourceName, String conflictingValue) {
        super(
                "CONFLICT-001",
                resourceName + " with value '" + conflictingValue + "' already exists.",
                List.of("The provided value for " + resourceName + " is not unique."),
                ZonedDateTime.now()
        );
    }
}
