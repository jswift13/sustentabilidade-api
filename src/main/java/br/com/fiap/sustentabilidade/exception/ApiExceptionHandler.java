package br.com.fiap.sustentabilidade.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.OffsetDateTime;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handle(ApiException ex) {
        var body = Map.of(
                "timestamp", OffsetDateTime.now().toString(),
                "status", ex.getStatus().value(),
                "error", ex.getStatus().getReasonPhrase(),
                "message", ex.getMessage()
        );
        return ResponseEntity.status(ex.getStatus()).body(body);
    }
}
