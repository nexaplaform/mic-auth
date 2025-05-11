package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.List;

public class OperationNotAllowedException extends BaseException {

    public OperationNotAllowedException(String message) {
        super("NA001", message, null, ZonedDateTime.now());
    }

    public OperationNotAllowedException(String resourceName, Object resourceId, String operation, String currentState) {
        super(
                "NA001",
                "Operation '" + operation + "' no permitida para " + resourceName + " con ID " + resourceId + " en estado '" + currentState + "'.",
                List.of("Revisar el estado actual del recurso."),
                ZonedDateTime.now()
        );
    }

    public OperationNotAllowedException(String message, List<String> reasons) {
        super("NA001", message, reasons, ZonedDateTime.now());
    }
}
