package com.losalgarrobos.socioclub.controller.api;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.service.ActividadService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// @RestController = @Controller + @ResponseBody: lo que devuelve cada método se convierte a JSON
// (no se busca una vista Thymeleaf). Usa el MISMO ActividadService que el controlador de la secretaría.
@RestController
@RequestMapping("/api/actividades")
public class ActividadRestController {

    private final ActividadService actividadService;

    public ActividadRestController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    // GET /api/actividades  y  GET /api/actividades?disciplina=Fútbol  -> 200
    @GetMapping
    public List<Actividad> listar(@RequestParam(required = false) String disciplina) {
        return actividadService.listarConCupo(disciplina);
    }

    // GET /api/actividades/3 -> 200, o 404 si no existe (lo resuelve ManejadorErroresApi)
    @GetMapping("/{id}")
    public Actividad detalle(@PathVariable Long id) {
        return actividadService.completarCupo(actividadService.buscarPorId(id));
    }

    // POST /api/actividades con la actividad en el cuerpo (JSON) -> 201 Created
    @PostMapping
    public ResponseEntity<Actividad> crear(@RequestBody Actividad actividad) {
        actividad.setId(null); // el id lo genera la base, no el cliente
        Actividad creada = actividadService.guardar(actividad);
        return ResponseEntity.status(HttpStatus.CREATED).body(actividadService.completarCupo(creada));
    }

    // PUT /api/actividades/3 -> 200, o 404 si no existe
    @PutMapping("/{id}")
    public Actividad modificar(@PathVariable Long id, @RequestBody Actividad actividad) {
        actividadService.buscarPorId(id); // si no existe lanza la excepción -> 404
        actividad.setId(id);
        return actividadService.completarCupo(actividadService.guardar(actividad));
    }

    // DELETE /api/actividades/3 -> 204 No Content, 404 si no existe, 409 si tiene inscriptos activos
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        actividadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
