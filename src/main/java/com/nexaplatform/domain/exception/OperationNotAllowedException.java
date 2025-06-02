package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.List;

public class OperationNotAllowedException extends BaseException {

    public OperationNotAllowedException(String code, String message) {
        super(code, message, null, ZonedDateTime.now());
    }

    public OperationNotAllowedException(String code, String resourceName, Object resourceId, String operation, String currentState) {
        super(
                code,
                "Operation '" + operation + "' no permitida para " + resourceName + " con ID " + resourceId + " en estado '" + currentState + "'.",
                List.of("Revisar el estado actual del recurso."),
                ZonedDateTime.now()
        );
    }

    public OperationNotAllowedException(String code, String message, List<String> reasons) {
        super(code, message, reasons, ZonedDateTime.now());
    }
}
