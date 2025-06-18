package com.nexaplatform.domain.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CodeError {

    public static final String ERROR_CODE_NOT_FOUND = "RNF001";
    public static final String ERROR_CODE_RESOURCE_ALREADY_EXISTS = "RAE001";
    public static final String ERROR_CODE_OPERATION_NOT_ALLOWED = "ONA001";
    public static final String ERROR_CODE_BUSINESS_RULE_VIOLATION = "BRV001";
    public static final String ERROR_CODE_METHOD_NOT_ALLOWED = "MNA001";
    public static final String ERROR_CODE_ARGUMENT_NOT_VALID = "BR001";
    public static final String ERROR_CODE_MESSAGE_NOT_READABLE = "MNR001";
    public static final String ERROR_CODE_JSON_MAPPING_ERROR = "JME001";
    public static final String ERROR_CODE_JSON_PARSE_ERROR = "JPE001";
    public static final String ERROR_CODE_TYPE_MISMATCH = "TM001";
    public static final String ERROR_CODE_MISSING_PARAMETER = "MP001";
    public static final String ERROR_CODE_INTERNAL_ERROR = "ISE001";
    public static final String ERROR_CODE_ACCESS_DENIED = "AUTH001";
}
