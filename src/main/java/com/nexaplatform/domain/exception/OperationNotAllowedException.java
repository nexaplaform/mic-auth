package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.List;

public class OperationNotAllowedException extends BaseException {

    public OperationNotAllowedException(String message) {
        super("NOT_ALLOWED-001", message, null, ZonedDateTime.now());
    }

    public OperationNotAllowedException(String resourceName, Object resourceId, String operation, String currentState) {
        super(
                "NOT_ALLOWED-001",
                "Operation '" + operation + "' not allowed for " + resourceName + " with ID " + resourceId + " in state '" + currentState + "'.",
                List.of("Review the current state of the resource."),
                ZonedDateTime.now()
        );
    }

    public OperationNotAllowedException(String message, List<String> reasons) {
        super("NOT_ALLOWED-001", message, reasons, ZonedDateTime.now());
    }
}
