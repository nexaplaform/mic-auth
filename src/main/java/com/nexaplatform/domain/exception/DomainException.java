package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class DomainException extends BaseException {

    public DomainException(String code, String message) {
        super(code, message, new ArrayList<>(), ZonedDateTime.now());
    }

    public DomainException(String code, String message, List<String> violations) {
        super(code, message, violations, ZonedDateTime.now());
    }
}
