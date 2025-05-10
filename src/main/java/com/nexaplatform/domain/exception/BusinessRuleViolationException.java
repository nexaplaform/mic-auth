package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class BusinessRuleViolationException extends BaseException {

    public BusinessRuleViolationException(String message) {
        super("BUSINESS-001", message, new ArrayList<>(), ZonedDateTime.now());
    }

    public BusinessRuleViolationException(String message, List<String> violations) {
        super("BUSINESS-001", message, violations, ZonedDateTime.now());
    }
}
