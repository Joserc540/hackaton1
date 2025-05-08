package org.dbp.hackaton.hackaton1.exception;

import org.dbp.hackaton.hackaton1.config.exception.GlobalExceptionHandler;
import org.dbp.hackaton.hackaton1.config.exception.ResourceNotFoundException;
import org.dbp.hackaton.hackaton1.config.exception.LimitExceededException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.core.MethodParameter;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.lang.reflect.Method;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleResourceNotFound() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Recurso no encontrado");
        ResponseEntity<Object> response = handler.handleResourceNotFound(ex);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = castBody(response);
        assertEquals(404, body.get("status"));
        assertEquals("Not Found", body.get("error"));
        assertEquals("Recurso no encontrado", body.get("message"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertFalse(body.containsKey("details"));
    }

    @Test
    void testHandleValidationErrors() throws Exception {
        TestDto dto = new TestDto();
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(dto, "testDto");
        bindingResult.addError(new FieldError("testDto", "field1", "no puede ser nulo"));

        Method m = TestController.class.getMethod("dummy", TestDto.class);
        MethodParameter parameter = new MethodParameter(m, 0);

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(parameter, bindingResult);
        ResponseEntity<Object> response = handler.handleValidationErrors(ex);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        Map<String, Object> body = castBody(response);
        assertEquals(400, body.get("status"));
        assertEquals("Bad Request", body.get("error"));
        assertEquals("Errores de validación", body.get("message"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);

        @SuppressWarnings("unchecked")
        Map<String, String> details = (Map<String, String>) body.get("details");
        assertNotNull(details);
        assertEquals(1, details.size());
        assertEquals("no puede ser nulo", details.get("field1"));
    }

    @Test
    void testHandleBadCredentials() {
        BadCredentialsException ex = new BadCredentialsException("invalid credentials");
        ResponseEntity<Object> response = handler.handleBadCredentials(ex);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        Map<String, Object> body = castBody(response);
        assertEquals(401, body.get("status"));
        assertEquals("Unauthorized", body.get("error"));
        assertEquals("Credenciales inválidas", body.get("message"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertFalse(body.containsKey("details"));
    }

    @Test
    void testHandleAccessDenied() {
        AccessDeniedException ex = new AccessDeniedException("someFile.txt");
        ResponseEntity<Object> response = handler.handleAccessDenied(ex);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        Map<String, Object> body = castBody(response);
        assertEquals(403, body.get("status"));
        assertEquals("Forbidden", body.get("error"));
        assertEquals("Acceso denegado", body.get("message"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertFalse(body.containsKey("details"));
    }

    @Test
    void testHandleGeneralException() {
        RuntimeException ex = new RuntimeException("algo salió mal");
        ResponseEntity<Object> response = handler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = castBody(response);
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("Error interno del servidor", body.get("message"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertFalse(body.containsKey("details"));
    }

    @Test
    void testLimitExceededFallsToGeneralHandler() {
        LimitExceededException ex = new LimitExceededException("límite excedido");
        ResponseEntity<Object> response = handler.handleGeneralException(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        Map<String, Object> body = castBody(response);
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("Error interno del servidor", body.get("message"));
        assertTrue(body.get("timestamp") instanceof LocalDateTime);
        assertFalse(body.containsKey("details"));
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castBody(ResponseEntity<Object> response) {
        assertNotNull(response.getBody());
        return (Map<String, Object>) response.getBody();
    }

    private static class TestDto {
        private String field1;
        public String getField1() { return field1; }
        public void setField1(String field1) { this.field1 = field1; }
    }

    private static class TestController {
        public void dummy(TestDto testDto) { /* no-op */ }
    }
}
