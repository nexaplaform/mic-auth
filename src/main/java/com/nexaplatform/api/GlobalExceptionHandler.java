package com.nexaplatform.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.nexaplatform.api.controllers.services.dto.out.ErrorResponse;
import com.nexaplatform.domain.exception.BusinessRuleViolationException;
import com.nexaplatform.domain.exception.EntityNotFoundException;
import com.nexaplatform.domain.exception.OperationNotAllowedException;
import com.nexaplatform.domain.exception.ResourceAlreadyExistsException;
import jakarta.annotation.Nullable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.nexaplatform.domain.exception.CodeError.*;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public static final String URI = "uri=";
    public static final String ENDPOINT_URL = "Url:";
    private static final String LOG_REQUEST_DESCRIPTION_PLACEHOLDER = " %s";
    private static final String LOG_EXCEPTION_MESSAGE_PLACEHOLDER = "{}";
    private static final String LOG_MESSAGE_FORMAT = "{} ocurrio para la solicitud " + LOG_REQUEST_DESCRIPTION_PLACEHOLDER + ": " + LOG_EXCEPTION_MESSAGE_PLACEHOLDER;
    private static final String DETAIL_ENDPOINT_FORMAT = ENDPOINT_URL + " " + LOG_REQUEST_DESCRIPTION_PLACEHOLDER;
    private static final String DETAIL_PROPERTY_VALIDATION_FORMAT = "La propiedad '%s' %s o el tipo de dato es incorrecto, se recomienda mirar la documentación.";
    private static final String DETAIL_OBJECT_VALIDATION_FORMAT = "Object Error: %s - %s";
    private static final String DETAIL_METHOD_NOT_SUPPORTED_FORMAT = "Metodo '%s' no soportado para este endpoint";
    private static final String DETAIL_SUPPORTED_METHODS_FORMAT = "Metodos soportados: %s";
    private static final String DETAIL_JSON_PARSE_LOCATION_FORMAT = "Ubicacion: linea %d, columna %d";
    private static final String DETAIL_CAUSE_FORMAT = "Cause: %s - %s";
    private static final String DETAIL_REQUEST_PATH_FORMAT = "Request path: %s";
    private static final String DETAIL_TYPE_MISMATCH_FORMAT = "El parámetro '%s' con valor '%s' no se pudo convertir al tipo requerido '%s'";
    private static final String DETAIL_MISSING_PARAMETER_FORMAT = "Falta el parámetro de solicitud requerido '%s' del tipo '%s'";
    private static final String DETAIL_JSON_MAPPING_PROBLEM_DETAIL_FORMAT = "Details: %s";
    private static final String DETAIL_JSON_MAPPING_FIELD_PATH_FORMAT = "Problematic field path: %s";
    private static final String DETAIL_JSON_MAPPING_EXPECTED_TYPE_FORMAT = "Expected type: %s";

    private static final String ERROR_MESSAGE_ENTITY_NOT_FOUND = "Entidad no encontrada";
    private static final String ERROR_MESSAGE_RESOURCE_ALREADY_EXISTS = "El recurso ya existe";
    private static final String ERROR_MESSAGE_OPERATION_NOT_ALLOWED = "Operación no permitida";
    private static final String ERROR_MESSAGE_BUSINESS_RULE_VIOLATION = "Violación de regla de negocio";
    private static final String ERROR_MESSAGE_METHOD_NOT_SUPPORTED = "Método HTTP no soportado";
    private static final String ERROR_MESSAGE_VALIDATION_FAILED = "Validación de petición fallida";
    private static final String ERROR_MESSAGE_MESSAGE_NOT_READABLE = "El cuerpo de la solicitud no es legible";
    private static final String ERROR_MESSAGE_JSON_MAPPING_ERROR = "Error procesando el cuerpo de la solicitud";
    private static final String ERROR_MESSAGE_JSON_PARSE_ERROR = "JSON malformado en el cuerpo de la solicitud";
    private static final String ERROR_MESSAGE_TYPE_MISMATCH = "El tipo de parámetro de solicitud no coincide";
    private static final String ERROR_MESSAGE_MISSING_PARAMETER = "Falta el parámetro de solicitud requerido";
    public static final String ERROR_MESSAGE_ACCESS_DENIED = "Acceso denegado: No tienes los permisos necesarios.";


    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {

        logDetail(ex.getClass().getSimpleName(), request, ex.getMessage());

        List<String> details = new ArrayList<>();
        details.add(String.format(DETAIL_ENDPOINT_FORMAT, request.getDescription(false)
                .replace(URI, "")));

        details.add("Causa: " + ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ERROR_CODE_ACCESS_DENIED)
                .message(ERROR_MESSAGE_ACCESS_DENIED)
                .details(details)
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }


    /**
     * Handles EntityNotFoundException.
     * Logs the exception and returns a 404 Not Found with BaseException details in ErrorResponse format.
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex, WebRequest request) {

        logDetail(ex.getClass().getSimpleName(), request, ex.getMessage());

        List<String> combinedDetails = Objects.nonNull(ex.getDetails()) ? new ArrayList<>(ex.getDetails()) : new ArrayList<>();
        combinedDetails.add(String.format(DETAIL_ENDPOINT_FORMAT, request.getDescription(false)
                .replace(URI, "")));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(Objects.nonNull(ex.getCode()) ? ex.getCode() : ERROR_CODE_NOT_FOUND)
                .message(Objects.nonNull(ex.getMessage()) ? ex.getMessage() : ERROR_MESSAGE_ENTITY_NOT_FOUND)
                .details(combinedDetails)
                .timeStamp(Objects.nonNull(ex.getTimeStamp()) ? ex.getTimeStamp() : ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles ResourceAlreadyExistsException.
     * Logs the exception and returns a 409 Conflict with BaseException details in ErrorResponse format.
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(
            ResourceAlreadyExistsException ex, WebRequest request) {

        logDetail(ex.getClass().getSimpleName(), request, ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(Objects.nonNull(ex.getCode()) ? ex.getCode() : ERROR_CODE_RESOURCE_ALREADY_EXISTS)
                .message(Objects.nonNull(ex.getMessage()) ? ex.getMessage() : ERROR_MESSAGE_RESOURCE_ALREADY_EXISTS)
                .details(ex.getDetails())
                .timeStamp(Objects.nonNull(ex.getTimeStamp()) ? ex.getTimeStamp() : ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    private static void logDetail(String ex, WebRequest request, String ex1) {
        log.warn(LOG_MESSAGE_FORMAT,
                ex,
                request.getDescription(true).replace(URI, ""),
                ex1);
    }

    /**
     * Handles OperationNotAllowedException.
     * Logs the exception and returns a 400 Bad Request (or 409 Conflict based on specific rule) with BaseException details in ErrorResponse format.
     */
    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleOperationNotAllowedException(
            OperationNotAllowedException ex, WebRequest request) {

        logDetail(ex.getClass().getSimpleName(), request, ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(Objects.nonNull(ex.getCode()) ? ex.getCode() : ERROR_CODE_OPERATION_NOT_ALLOWED)
                .message(Objects.nonNull(ex.getMessage()) ? ex.getMessage() : ERROR_MESSAGE_OPERATION_NOT_ALLOWED)
                .details(ex.getDetails())
                .timeStamp(Objects.nonNull(ex.getTimeStamp()) ? ex.getTimeStamp() : ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles BusinessRuleViolationException.
     * Logs the exception and returns a 400 Bad Request with BaseException details in ErrorResponse format.
     */
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolationException(
            BusinessRuleViolationException ex, WebRequest request) {

        logDetail(ex.getClass().getSimpleName(), request, ex.getMessage());

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(Objects.nonNull(ex.getCode()) ? ex.getCode() : ERROR_CODE_BUSINESS_RULE_VIOLATION)
                .message(Objects.nonNull(ex.getMessage()) ? ex.getMessage() : ERROR_MESSAGE_BUSINESS_RULE_VIOLATION)
                .details(ex.getDetails())
                .timeStamp(Objects.nonNull(ex.getTimeStamp()) ? ex.getTimeStamp() : ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Overrides the handling of HttpRequestMethodNotSupportedException (405 Method Not Allowed).
     * Returns an ErrorResponse with details about the unsupported method and allowed methods.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        List<String> details = new java.util.ArrayList<>();
        details.add(String.format(DETAIL_METHOD_NOT_SUPPORTED_FORMAT, ex.getMethod()));

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (Objects.nonNull(supportedMethods) && !supportedMethods.isEmpty()) {
            details.add(String.format(DETAIL_SUPPORTED_METHODS_FORMAT, supportedMethods));
        }

        details.add(String.format(DETAIL_ENDPOINT_FORMAT, request.getDescription(false)
                .replace(URI, "")));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ERROR_CODE_METHOD_NOT_ALLOWED)
                .message(ERROR_MESSAGE_METHOD_NOT_SUPPORTED)
                .details(details)
                .timeStamp(ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }

    /**
     * Overrides the handling of MethodArgumentNotValidException (400 Bad Request due to validation).
     * Returns an ErrorResponse with details of field validation errors.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        List<String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> String.format(DETAIL_PROPERTY_VALIDATION_FORMAT, error.getField(),
                        error.getDefaultMessage()))
                .collect(Collectors.toList());

        ex.getBindingResult().getGlobalErrors().forEach(error ->
                validationErrors.add(String.format(DETAIL_OBJECT_VALIDATION_FORMAT, error.getObjectName(),
                        error.getDefaultMessage()))
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ERROR_CODE_ARGUMENT_NOT_VALID)
                .message(ERROR_MESSAGE_VALIDATION_FAILED)
                .details(validationErrors)
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }


    private record ExceptionDetails(
            String errorCode,
            String baseErrorMessage,
            List<String> details
    ) {
    }

    /**
     * Overrides the handling of HttpMessageNotReadableException (400 Bad Request).
     * Handles issues during JSON deserialization (malformed, incorrect type, missing required field).
     * Returns a detailed ErrorResponse.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());
        Throwable mostSpecificCause = ex.getMostSpecificCause();

        ExceptionDetails exceptionDetails = switch (mostSpecificCause) {
            case JsonMappingException jsonMappingException -> handleJsonMappingException(jsonMappingException);
            case JsonParseException jsonParseException -> handleJsonParseException(jsonParseException);
            default -> handleGenericException(mostSpecificCause);
        };


        String errorCode = exceptionDetails.errorCode();
        String baseErrorMessage = exceptionDetails.baseErrorMessage();
        List<String> details = new ArrayList<>(exceptionDetails.details());

        if (mostSpecificCause instanceof JsonMappingException jsonMappingException) {
            String path = jsonMappingException.getPath().stream()
                    .map(ref -> {
                        if (Objects.nonNull(ref.getFieldName())) {
                            return ref.getFieldName();
                        } else {
                            return "[" + ref.getIndex() + "]";
                        }
                    })
                    .collect(Collectors.joining("."));
            if (!path.isEmpty()) {
                baseErrorMessage = String.format("Error en el campo '%s'", path);
            }
        }

        details.add(String.format(DETAIL_REQUEST_PATH_FORMAT, request.getDescription(false).replace(URI, "")));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(errorCode)
                .message(baseErrorMessage)
                .details(details)
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }

    private ExceptionDetails handleJsonMappingException(JsonMappingException jsonMappingException) {

        List<String> details = new ArrayList<>();

        String path = jsonMappingException.getPath().stream()
                .map(ref -> Objects.nonNull(ref.getFieldName()) ? ref.getFieldName() : "[" + ref.getIndex() + "]")
                .collect(Collectors.joining("."));

        if (!path.isEmpty()) {
            details.add(String.format(DETAIL_JSON_MAPPING_PROBLEM_DETAIL_FORMAT,
                    jsonMappingException.getOriginalMessage()));
            details.add(String.format(DETAIL_JSON_MAPPING_FIELD_PATH_FORMAT, path));

            if (jsonMappingException instanceof MismatchedInputException mismatchEx
                    && Objects.nonNull(mismatchEx.getTargetType())) {
                details.add(String.format(DETAIL_JSON_MAPPING_EXPECTED_TYPE_FORMAT,
                        mismatchEx.getTargetType().getSimpleName()));
            }
        } else {
            details.add(String.format(DETAIL_JSON_MAPPING_PROBLEM_DETAIL_FORMAT,
                    jsonMappingException.getOriginalMessage()));
        }

        return new ExceptionDetails(ERROR_CODE_JSON_MAPPING_ERROR, ERROR_MESSAGE_JSON_MAPPING_ERROR, details);
    }

    private ExceptionDetails handleJsonParseException(JsonParseException jsonParseException) {

        List<String> details = new ArrayList<>();

        details.add(String.format(DETAIL_JSON_PARSE_LOCATION_FORMAT,
                jsonParseException.getLocation().getLineNr(), jsonParseException.getLocation().getColumnNr()));
        details.add(String.format(DETAIL_JSON_MAPPING_PROBLEM_DETAIL_FORMAT,
                jsonParseException.getOriginalMessage()));

        return new ExceptionDetails(ERROR_CODE_JSON_PARSE_ERROR, ERROR_MESSAGE_JSON_PARSE_ERROR, details);
    }

    private ExceptionDetails handleGenericException(Throwable mostSpecificCause) {
        List<String> details = new ArrayList<>();

        details.add(String.format(DETAIL_CAUSE_FORMAT, mostSpecificCause.getClass().getSimpleName(),
                mostSpecificCause.getMessage()));

        return new ExceptionDetails(ERROR_CODE_MESSAGE_NOT_READABLE, ERROR_MESSAGE_MESSAGE_NOT_READABLE, details);
    }

    /**
     * Overrides the handling of TypeMismatchException (400 Bad Request due to incorrect data type).
     * Returns an ErrorResponse with details about the field and involved types.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        String detailMessage = getDetailMessage(ex);

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ERROR_CODE_TYPE_MISMATCH)
                .message(ERROR_MESSAGE_TYPE_MISMATCH)
                .details(List.of(detailMessage))
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }

    private static String getDetailMessage(TypeMismatchException ex) {
        String propertyName = Objects.nonNull(ex.getPropertyName()) ? ex.getPropertyName() : "Propiedad desconocida";
        Object valueReceived = ex.getValue();
        Class<?> requiredType = ex.getRequiredType();
        String requiredTypeName = Objects.nonNull(requiredType) ? requiredType.getSimpleName() : "Tipo desconocido";

        return String.format(
                DETAIL_TYPE_MISMATCH_FORMAT,
                propertyName,
                (Objects.nonNull(valueReceived) ? String.valueOf(valueReceived) : "null"),
                requiredTypeName
        );
    }

    /**
     * Overrides the handling of MissingServletRequestParameterException (400 Bad Request due to missing parameter).
     * Returns an ErrorResponse indicating which required parameter is missing.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        String parameterName = ex.getParameterName();
        String parameterType = ex.getParameterType();

        String detailMessage = String.format(
                DETAIL_MISSING_PARAMETER_FORMAT,
                parameterName,
                parameterType
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ERROR_CODE_MISSING_PARAMETER)
                .message(ERROR_MESSAGE_MISSING_PARAMETER)
                .details(List.of(detailMessage))
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }
}
