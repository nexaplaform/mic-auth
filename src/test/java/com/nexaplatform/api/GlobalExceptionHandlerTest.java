package com.nexaplatform.api;

import com.nexaplatform.api.controllers.services.dto.out.ErrorResponse;
import com.nexaplatform.domain.exception.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.context.request.WebRequest;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonMappingException;

import java.util.Collections;
import java.util.List;

import static com.nexaplatform.domain.errors.Error.USER_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private static final String ERROR_CODE_ACCESS_DENIED = "ACCESS_DENIED";
    private static final String ERROR_CODE_NOT_FOUND = "NOT_FOUND";
    private static final String ERROR_CODE_RESOURCE_ALREADY_EXISTS = "RESOURCE_EXISTS";
    private static final String ERROR_CODE_OPERATION_NOT_ALLOWED = "OP_NOT_ALLOWED";
    private static final String ERROR_CODE_BUSINESS_RULE_VIOLATION = "BUSINESS_RULE_VIOLATION";
    private static final String ERROR_CODE_METHOD_NOT_ALLOWED = "METHOD_NOT_ALLOWED";
    private static final String ERROR_CODE_ARGUMENT_NOT_VALID = "ARG_NOT_VALID";
    private static final String ERROR_CODE_MESSAGE_NOT_READABLE = "MSG_NOT_READABLE";
    private static final String ERROR_CODE_JSON_MAPPING_ERROR = "JSON_MAPPING_ERROR";
    private static final String ERROR_CODE_JSON_PARSE_ERROR = "JSON_PARSE_ERROR";
    private static final String ERROR_CODE_TYPE_MISMATCH = "TYPE_MISMATCH";
    private static final String ERROR_CODE_MISSING_PARAMETER = "MISSING_PARAM";

    @Mock
    private WebRequest webRequest;

    @Mock
    private HttpHeaders httpHeaders;

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    void handleAccessDeniedException_shouldReturnForbidden() {

        AccessDeniedException ex = new AccessDeniedException("Access denied test message");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/test");
        when(webRequest.getDescription(true)).thenReturn("uri=/api/test [client=127.0.0.1]");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleAccessDeniedException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(CodeError.ERROR_CODE_ACCESS_DENIED, response.getBody().getCode());
        assertEquals(GlobalExceptionHandler.ERROR_MESSAGE_ACCESS_DENIED, response.getBody().getMessage());
        assertEquals(2, response.getBody().getDetails().size());
        assertTrue(response.getBody().getDetails().contains("Causa: Access denied test message"));
        assertNotNull(response.getBody().getTimeStamp());

        verify(webRequest, times(1)).getDescription(false);
        verify(webRequest, times(1)).getDescription(true);
    }

    @Test
    void handleEntityNotFoundException_withDetails_shouldReturnNotFound() {

        String customCode = "USER_NOT_FOUND";
        String customMessage = "User not found with ID 123";
        List<String> customDetails = List.of("Detail 1", "Detail 2");

        EntityNotFoundException ex = new EntityNotFoundException(customCode, customMessage, customDetails);
        when(webRequest.getDescription(false)).thenReturn("uri=/api/users/123");
        when(webRequest.getDescription(true)).thenReturn("uri=/api/users/123 [client=127.0.0.1]");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEntityNotFoundException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(USER_NOT_FOUND.toString(), response.getBody().getCode());
        assertEquals(customMessage, response.getBody().getMessage());
        assertEquals(3, response.getBody().getDetails().size()); // customDetails + endpoint detail
        assertTrue(response.getBody().getDetails().contains("Detail 1"));
        assertTrue(response.getBody().getDetails().contains("Detail 2"));

        verify(webRequest, times(1)).getDescription(false);
        verify(webRequest, times(1)).getDescription(true);
    }

    @Test
    void handleEntityNotFoundException_noCustomDetails_shouldReturnNotFound() {

        EntityNotFoundException ex = new EntityNotFoundException(ERROR_CODE_NOT_FOUND, "Generic not found");
        when(webRequest.getDescription(false)).thenReturn("uri=/api/products/456");
        when(webRequest.getDescription(true)).thenReturn("uri=/api/products/456 [client=127.0.0.1]");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleEntityNotFoundException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(ERROR_CODE_NOT_FOUND, response.getBody().getCode());
        assertEquals("Generic not found", response.getBody().getMessage());
        assertEquals(1, response.getBody().getDetails().size());
        assertNotNull(response.getBody().getTimeStamp());

        verify(webRequest, times(1)).getDescription(false);
        verify(webRequest, times(1)).getDescription(true);
    }

    @Test
    void handleResourceAlreadyExistsException_withDetails_shouldReturnConflict() {

        String customCode = "ITEM_EXISTS";
        String customMessage = "Item 'Test' already exists";

        ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException(customCode, customMessage);
        when(webRequest.getDescription(true)).thenReturn("uri=/api/items [client=127.0.0.1]");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceAlreadyExistsException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CT001", response.getBody().getCode());
        assertEquals("ITEM_EXISTS con valor 'Item 'Test' already exists' ya existe.", response.getBody().getMessage());

        verify(webRequest, times(1)).getDescription(true);
        verify(webRequest, never()).getDescription(false);
    }

    @Test
    void handleResourceAlreadyExistsException_noCustomDetails_shouldReturnConflict() {

        ResourceAlreadyExistsException ex = new ResourceAlreadyExistsException("El recurso ya existe");
        when(webRequest.getDescription(true)).thenReturn("uri=/api/items/new [client=127.0.0.1]");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleResourceAlreadyExistsException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CT001", response.getBody().getCode());
        assertEquals("El recurso ya existe", response.getBody().getMessage());
        assertEquals(Collections.emptyList(), response.getBody().getDetails());
        assertNotNull(response.getBody().getTimeStamp());

        verify(webRequest, times(1)).getDescription(true);
        verify(webRequest, never()).getDescription(false);
    }

    @Test
    void handleOperationNotAllowedException_withDetails_shouldReturnBadRequest() {

        String customCode = "INVALID_OPERATION";
        String customMessage = "Cannot delete active user";

        OperationNotAllowedException ex = new OperationNotAllowedException(customCode, customMessage);
        when(webRequest.getDescription(true)).thenReturn("uri=/api/users/delete [client=127.0.0.1]");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleOperationNotAllowedException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customCode, response.getBody().getCode());
        assertEquals(customMessage, response.getBody().getMessage());
    }

    @Test
    void handleBusinessRuleViolationException_withDetails_shouldReturnBadRequest() {

        String customCode = "INSUFFICIENT_FUNDS";
        String customMessage = "Account balance is too low";

        BusinessRuleViolationException ex = new BusinessRuleViolationException(customCode, customMessage);
        when(webRequest.getDescription(true)).thenReturn("uri=/api/transactions [client=127.0.0.1]");

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleBusinessRuleViolationException(ex, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(customCode, response.getBody().getCode());
        assertEquals(customMessage, response.getBody().getMessage());
    }

    @Test
    void handleMethodArgumentNotValid_shouldReturnBadRequest_withFieldAndGlobalErrors() {

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);

        when(ex.getBindingResult()).thenReturn(bindingResult);

        FieldError fieldError1 = new FieldError("object", "field1", "must not be null");
        FieldError fieldError2 = new FieldError("object", "field2", "size must be between 1 and 10");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        ObjectError globalError = new ObjectError("objectName", "Global error message");
        when(bindingResult.getGlobalErrors()).thenReturn(List.of(globalError));

        HttpHeaders actualHeaders = new HttpHeaders();
        HttpStatusCode status = HttpStatus.BAD_REQUEST;

        ResponseEntity<Object> response = globalExceptionHandler.handleMethodArgumentNotValid(ex, actualHeaders, status, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();

        assertEquals("BR001", errorResponse.getCode());
        assertEquals("Validación de petición fallida", errorResponse.getMessage());
        assertEquals(3, errorResponse.getDetails().size());
        assertTrue(errorResponse.getDetails().contains("La propiedad 'field1' must not be null o el tipo de dato es incorrecto, se recomienda mirar la documentación."));
        assertTrue(errorResponse.getDetails().contains("La propiedad 'field2' size must be between 1 and 10 o el tipo de dato es incorrecto, se recomienda mirar la documentación."));
        assertTrue(errorResponse.getDetails().contains("Object Error: objectName - Global error message"));
        assertNotNull(errorResponse.getTimeStamp());

        verifyNoInteractions(webRequest);
    }

    @Test
    void handleHttpMessageNotReadable_JsonMappingException_shouldReturnBadRequest() {

        String originalMessage = "Can not deserialize value of type `java.lang.Long` from String \"abc\": not a valid Long value";
        JsonMappingException jsonMappingException = new JsonMappingException(null, originalMessage);
        jsonMappingException.prependPath(new JsonMappingException.Reference(null, "someField"));

        HttpMessageNotReadableException ex = new HttpMessageNotReadableException(originalMessage, jsonMappingException, null);
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        HttpHeaders httpHeaders = new HttpHeaders();
        
        when(webRequest.getDescription(false)).thenReturn("uri=/api/data");

        ResponseEntity<Object> response = globalExceptionHandler.handleHttpMessageNotReadable(ex, httpHeaders, status, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();

        assertEquals("MNR001", errorResponse.getCode());
        assertEquals("El cuerpo de la solicitud no es legible", errorResponse.getMessage());
        assertEquals(2, errorResponse.getDetails().size());
        assertTrue(errorResponse.getDetails().contains("Cause: JsonMappingException - Can not deserialize value of type " +
                "`java.lang.Long` from String \"abc\": not a valid Long value (through reference chain: UNKNOWN[\"someField\"])"));
        assertTrue(errorResponse.getDetails().contains("Request path: /api/data"));
        assertNotNull(errorResponse.getTimeStamp());

        verify(webRequest, times(1)).getDescription(false);
    }


    @Test
    void handleMissingServletRequestParameter_shouldReturnBadRequest() {

        MissingServletRequestParameterException ex = new MissingServletRequestParameterException("param1", "String");
        HttpStatusCode status = HttpStatus.BAD_REQUEST;
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<Object> response = globalExceptionHandler.handleMissingServletRequestParameter(ex, headers, status, webRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertInstanceOf(ErrorResponse.class, response.getBody());
        ErrorResponse errorResponse = (ErrorResponse) response.getBody();

        assertEquals("MP001", errorResponse.getCode());
        assertEquals("Falta el parámetro de solicitud requerido", errorResponse.getMessage());
        assertEquals(1, errorResponse.getDetails().size());
        assertEquals("Falta el parámetro de solicitud requerido 'param1' del tipo 'String'", errorResponse.getDetails().getFirst());
        assertNotNull(errorResponse.getTimeStamp());

        verifyNoInteractions(webRequest);
    }
}