package id.ac.ui.cs.advprog.rating_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GlobalExceptionTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleRatingNotFoundException() {
        RatingNotFoundException ex = new RatingNotFoundException("Rating not found");
        ResponseEntity<Object> response = handler.handleRatingNotFoundException(ex);

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(404, body.get("status"));
        assertEquals("Rating Not Found", body.get("error"));
        assertEquals("Rating not found", body.get("message"));
    }

    @Test
    void testHandleIllegalArgumentException() {
        IllegalArgumentException ex = new IllegalArgumentException("Invalid input");
        ResponseEntity<Object> response = handler.handleIllegalArgumentException(ex);

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(400, body.get("status"));
        assertEquals("Invalid Argument", body.get("error"));
        assertEquals("Invalid input", body.get("message"));
    }

    @Test
    void testHandleAllExceptions() {
        Exception ex = new Exception("Unexpected");
        ResponseEntity<Object> response = handler.handleAllExceptions(ex);

        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals(500, body.get("status"));
        assertEquals("Internal Server Error", body.get("error"));
        assertEquals("Something went wrong", body.get("message"));
    }


}

