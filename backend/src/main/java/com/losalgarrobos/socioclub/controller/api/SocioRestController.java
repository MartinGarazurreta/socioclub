package com.losalgarrobos.socioclub.controller.api;

import com.losalgarrobos.socioclub.model.Inscripcion;
import com.losalgarrobos.socioclub.model.Socio;
import com.losalgarrobos.socioclub.service.InscripcionService;
import com.losalgarrobos.socioclub.service.SocioService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Endpoints que usa la app "Mi Club" para el ingreso del socio y sus inscripciones
@RestController
@RequestMapping("/api/socios")
public class SocioRestController {

    private final SocioService socioService;
    private final InscripcionService inscripcionService;

    public SocioRestController(SocioService socioService, InscripcionService inscripcionService) {
        this.socioService = socioService;
        this.inscripcionService = inscripcionService;
    }

    // GET /api/socios/dni/35123987 -> 200 con el socio, o 404 si no hay socio con ese DNI (RF8)
    @GetMapping("/dni/{dni}")
    public Socio buscarPorDni(@PathVariable String dni) {
        return socioService.buscarPorDni(dni);
    }

    // GET /api/socios/7/inscripciones -> 200 con las inscripciones ACTIVAS, o 404 si el socio no existe
    @GetMapping("/{id}/inscripciones")
    public List<Inscripcion> inscripcionesActivas(@PathVariable Long id) {
        return inscripcionService.listarActivasPorSocio(id);
    }
}
