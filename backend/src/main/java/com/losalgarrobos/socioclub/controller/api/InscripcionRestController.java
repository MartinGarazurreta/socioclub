package com.losalgarrobos.socioclub.controller.api;

import com.losalgarrobos.socioclub.model.Inscripcion;
import com.losalgarrobos.socioclub.service.InscripcionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Inscripciones desde la app de socios. Las reglas RN1 a RN5 las aplica InscripcionService,
// exactamente el mismo que usa la secretaría: si una regla falla, el servicio lanza
// ReglaNegocioException y ManejadorErroresApi la convierte en un 409 Conflict.
@RestController
@RequestMapping("/api/inscripciones")
public class InscripcionRestController {

    private final InscripcionService inscripcionService;

    public InscripcionRestController(InscripcionService inscripcionService) {
        this.inscripcionService = inscripcionService;
    }

    // POST /api/inscripciones  { "socioId": 3, "actividadId": 1 }  -> 201, 404 o 409
    @PostMapping
    public ResponseEntity<Inscripcion> inscribir(@RequestBody SolicitudInscripcion solicitud) {
        if (solicitud.getSocioId() == null || solicitud.getActividadId() == null) {
            throw new IllegalArgumentException("Hay que indicar socioId y actividadId.");
        }
        Inscripcion inscripcion = inscripcionService.inscribir(solicitud.getSocioId(), solicitud.getActividadId());
        return ResponseEntity.status(HttpStatus.CREATED).body(inscripcion);
    }

    // PUT /api/inscripciones/5/baja -> 200 con la inscripción en estado BAJA, o 404 (RF9)
    // Es PUT y no DELETE porque la inscripción no se borra: se modifica su estado (baja lógica).
    @PutMapping("/{id}/baja")
    public Inscripcion darDeBaja(@PathVariable Long id) {
        return inscripcionService.darDeBaja(id);
    }
}
