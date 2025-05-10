package com.nexaplatform.domain.exception;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class EntityNotFound extends BaseException {

    public EntityNotFound(String message) {
        super("NOTFOUND-001", message, new ArrayList<>(), ZonedDateTime.now());
    }

    public EntityNotFound(String message, List<String> violations) {
        super("NOTFOUND-001", message, violations, ZonedDateTime.now());
    }
}
