package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class BusinessRuleViolationException extends BaseException {

    public BusinessRuleViolationException(String code, String message) {
        super(code, message, new ArrayList<>(), ZonedDateTime.now());
    }

    public BusinessRuleViolationException(String code, String message, List<String> violations) {
        super(code, message, violations, ZonedDateTime.now());
    }
}
