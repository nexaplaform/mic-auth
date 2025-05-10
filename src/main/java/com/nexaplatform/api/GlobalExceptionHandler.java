package com.nexaplatform.api;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.nexaplatform.api.controllers.services.dto.out.ErrorResponse;
import com.nexaplatform.domain.exception.BusinessRuleViolationException;
import com.nexaplatform.domain.exception.EntityNotFound;
import com.nexaplatform.domain.exception.OperationNotAllowedException;
import com.nexaplatform.domain.exception.ResourceAlreadyExistsException;
import jakarta.annotation.Nullable;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Maneja excepciones de tipo EntityNotFound.
     * Loguea la excepcion y retorna un 404 Not Found con los detalles de la BaseException en el formato ErrorResponse.
     */
    @ExceptionHandler(EntityNotFound.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFound ex, WebRequest request) {

        log.warn("EntityNotFoundException occurred for request {}: {}",
                request.getDescription(true).replace("uri=", ""),
                ex.getMessage());

        List<String> combinedDetails = ex.getDetails() != null ? new java.util.ArrayList<>(ex.getDetails()) : new java.util.ArrayList<>();
        combinedDetails.add("Resource path: " + request.getDescription(false).replace("uri=", ""));
        List<String> finalDetails = combinedDetails.isEmpty() ? new ArrayList<>() : combinedDetails;

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .details(finalDetails)
                .timeStamp(ex.getTimeStamp() != null ? ex.getTimeStamp() : ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    /**
     * Maneja excepciones de tipo ResourceAlreadyExistsException.
     * Loguea la excepcion y retorna un 409 Conflict con los detalles de la BaseException en el formato ErrorResponse.
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleResourceAlreadyExistsException(ResourceAlreadyExistsException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .details(ex.getDetails())
                .timeStamp(ex.getTimeStamp() != null ? ex.getTimeStamp() : ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones de tipo OperationNotAllowedException.
     * Loguea la excepcion y retorna un 400 Bad Request (o 409 Conflict segun la regla especifica) con los detalles de la BaseException en el formato ErrorResponse.
     */
    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<ErrorResponse> handleOperationNotAllowedException(OperationNotAllowedException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .details(ex.getDetails())
                .timeStamp(ex.getTimeStamp() != null ? ex.getTimeStamp() : ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Maneja excepciones de tipo BusinessRuleViolationException.
     * Loguea la excepcion y retorna un 400 Bad Request con los detalles de la BaseException en el formato ErrorResponse.
     */
    @ExceptionHandler(BusinessRuleViolationException.class)
    public ResponseEntity<ErrorResponse> handleBusinessRuleViolationException(BusinessRuleViolationException ex, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(ex.getCode())
                .message(ex.getMessage())
                .details(ex.getDetails())
                .timeStamp(ex.getTimeStamp() != null ? ex.getTimeStamp() : ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Sobrescribe el manejo de HTTPRequestMethodNotSupportedException (405 Method Not Allowed).
     * Retorna un ErrorResponse con detalles sobre el método no soportado y los métodos permitidos.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        List<String> details = new java.util.ArrayList<>();
        details.add("Metodo '" + ex.getMethod() + "' no soportado para este endpoint");

        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (supportedMethods != null && !supportedMethods.isEmpty()) {
            details.add("Metodos soportados: " + supportedMethods);
        }

        details.add("Url: " + request.getDescription(false).replace("uri=", ""));

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("MNA001")
                .message("Metodo HTTP no soportado")
                .details(details)
                .timeStamp(ZonedDateTime.now())
                .build();
        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }

    /**
     * Sobrescribe el manejo de MethodArgumentNotValidException (400 Bad Request por validacion).
     * Retorna un ErrorResponse con detalles de los errores de validacion de los campos.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value()); // Convierte HttpStatusCode a HttpStatus

        List<String> validationErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> "La propiedad '" + error.getField() + "' " + error.getDefaultMessage())
                .collect(Collectors.toList());

        ex.getBindingResult().getGlobalErrors().forEach(error ->
                validationErrors.add("Object Error: " + error.getObjectName() + " - " + error.getDefaultMessage())
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("BR001")
                .message("Validación de petición")
                .details(validationErrors)
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }

    /**
     * Sobrescribe el manejo de HttpMessageNotReadableException (400 Bad Request por problemas en el cuerpo de la peticion).
     * Esto ocurre durante la deserializacion (JSON mal formado, campo requerido ausente, tipo incorrecto).
     * Retorna un ErrorResponse con detalles sobre el error de lectura del mensaje.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        String errorMessage = "El cuerpo de la solicitud no se puede leer";
        List<String> details = new java.util.ArrayList<>();
        details.add("Path: " + request.getDescription(false).replace("uri=", ""));

        Throwable mostSpecificCause = ex.getMostSpecificCause();
        if (mostSpecificCause != null) {
            errorMessage = mostSpecificCause.getMessage();
            if (mostSpecificCause instanceof JsonParseException) {
                details.add("JSON malformed: " + mostSpecificCause.getMessage());
            } else if (mostSpecificCause instanceof JsonMappingException) {
                details.add("JSON mapping error: " + mostSpecificCause.getMessage());
            }
        } else {
            errorMessage = ex.getMessage();
        }

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("MNR001")
                .message(errorMessage)
                .details(details)
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }

    /**
     * Sobrescribe el manejo de TypeMismatchException (400 Bad Request por tipo de dato incorrecto).
     * Retorna un ErrorResponse con detalles sobre el campo y los tipos involucrados.
     */
    @Override // Indica que estas sobrescribiendo un metodo de la superclase
    @Nullable // Mantén la anotacion Nullable si estaba en la firma original
    protected ResponseEntity<Object> handleTypeMismatch(
            TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value()); // Convertir HttpStatusCode a HttpStatus

        String propertyName = ex.getPropertyName() != null ? ex.getPropertyName() : "Propiedad desconocida";
        Object valueReceived = ex.getValue();
        Class<?> requiredType = ex.getRequiredType();
        String requiredTypeName = requiredType != null ? requiredType.getSimpleName() : "Tipo desconocido";

        String detailMessage = String.format(
                "El parámetro '%s' con valor '%s' no se pudo convertir al tipo requerido '%s'",
                propertyName,
                (valueReceived != null ? String.valueOf(valueReceived) : "null"),
                requiredTypeName
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("TM001")
                .message("El tipo de parámetro de solicitud no coincide")
                .details(List.of(detailMessage))
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }

    /**
     * Sobrescribe el manejo de MissingServletRequestParameterException (400 Bad Request por parametro faltante).
     * Retorna un ErrorResponse indicando qué parametro requerido falta.
     */
    @Override
    @Nullable
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        String parameterName = ex.getParameterName();
        String parameterType = ex.getParameterType();

        String detailMessage = String.format(
                "Falta el parámetro de solicitud requerido '%s' del tipo '%s'",
                parameterName,
                parameterType
        );

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("MP001")
                .message("Falta el parámetro de solicitud requerido")
                .details(List.of(detailMessage))
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, headers, httpStatus);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAllOtherExceptions(Exception ex, WebRequest request) {
        System.err.println("Caught unhandled exception: " + ex.getMessage());
        // ex.printStackTrace(); // Usar logger en produccion

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                // .details(List.of(ex.getMessage()))
                .timeStamp(ZonedDateTime.now())
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
