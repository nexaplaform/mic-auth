package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntityNotFoundException extends BaseException {

    public EntityNotFoundException(String code, String message) {
        super(code, message, new ArrayList<>(), ZonedDateTime.now());
    }

    public EntityNotFoundException(String code, String message, List<String> violations) {
        super(code, message, violations, ZonedDateTime.now());
    }
}
