package com.losalgarrobos.socioclub.controller.api;

import com.losalgarrobos.socioclub.service.RecursoNoEncontradoException;
import com.losalgarrobos.socioclub.service.ReglaNegocioException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

// Convierte las excepciones del servicio en respuestas JSON con el código HTTP que corresponde.
// basePackages: solo atiende a los controladores REST de este paquete; las pantallas Thymeleaf
// siguen manejando sus errores mostrando el mensaje en la vista.
@RestControllerAdvice(basePackages = "com.losalgarrobos.socioclub.controller.api")
public class ManejadorErroresApi {

    // Una regla de negocio rechazó la operación -> 409 Conflict
    // { "error": "La actividad no tiene cupo disponible", "regla": "RN1" }
    @ExceptionHandler(ReglaNegocioException.class)
    public ResponseEntity<Map<String, String>> reglaNegocio(ReglaNegocioException e) {
        Map<String, String> cuerpo = new LinkedHashMap<>();
        cuerpo.put("error", e.getMessage());
        cuerpo.put("regla", e.getRegla());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(cuerpo);
    }

    // Socio, actividad o inscripción inexistente -> 404 Not Found
    @ExceptionHandler(RecursoNoEncontradoException.class)
    public ResponseEntity<Map<String, String>> noEncontrado(RecursoNoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
    }

    // Datos inválidos (edades, cupo, DNI, campos faltantes) -> 400 Bad Request
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> datosInvalidos(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
    }

    // El cuerpo no es un JSON válido -> 400 Bad Request
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> jsonInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.badRequest().body(Map.of("error", "El cuerpo de la petición no es un JSON válido."));
    }
}
