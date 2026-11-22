package com.losalgarrobos.socioclub.controller;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.model.Inscripcion;
import com.losalgarrobos.socioclub.service.ActividadService;
import com.losalgarrobos.socioclub.service.InscripcionService;
import com.losalgarrobos.socioclub.service.ReglaNegocioException;
import com.losalgarrobos.socioclub.service.SocioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// Controlador MVC de la secretaría para inscribir socios (RF6) y darlos de baja (RF9).
// Las reglas RN1 a RN5 NO están acá: las aplica InscripcionService. El controlador solo
// atrapa la ReglaNegocioException y muestra su mensaje en la vista.
@Controller
@RequestMapping("/inscripciones")
public class InscripcionController {

    private final InscripcionService inscripcionService;
    private final SocioService socioService;
    private final ActividadService actividadService;

    public InscripcionController(InscripcionService inscripcionService,
                                 SocioService socioService,
                                 ActividadService actividadService) {
        this.inscripcionService = inscripcionService;
        this.socioService = socioService;
        this.actividadService = actividadService;
    }

    // Formulario de inscripción. socioId y actividadId son opcionales: sirven para llegar
    // con el socio o la actividad ya elegidos (desde el listado de socios o de actividades).
    @GetMapping("/nueva")
    public String nueva(@RequestParam(required = false) Long socioId,
                        @RequestParam(required = false) Long actividadId,
                        Model model) {
        prepararFormulario(model, socioId, actividadId);
        return "inscripciones/formulario";
    }

    @PostMapping
    public String inscribir(@RequestParam Long socioId,
                            @RequestParam Long actividadId,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        try {
            Inscripcion inscripcion = inscripcionService.inscribir(socioId, actividadId);
            redirectAttributes.addFlashAttribute("mensaje", inscripcion.getSocio().getNombreCompleto()
                    + " quedó inscripto/a en " + inscripcion.getActividad().getNombre() + ".");
            return "redirect:/actividades/" + actividadId + "/inscriptos";
        } catch (ReglaNegocioException e) {
            // La regla rechazó la inscripción: se vuelve a mostrar el formulario con el mensaje
            model.addAttribute("error", e.getMessage());
            model.addAttribute("regla", e.getRegla());
            prepararFormulario(model, socioId, actividadId);
            return "inscripciones/formulario";
        }
    }

    // Baja lógica (RF9): la inscripción no se borra, pasa a estado BAJA.
    // volverA indica a qué pantalla regresar: la ficha del socio o los inscriptos de la actividad.
    @PostMapping("/{id}/baja")
    public String darDeBaja(@PathVariable Long id,
                            @RequestParam(defaultValue = "actividad") String volverA,
                            RedirectAttributes redirectAttributes) {
        Inscripcion inscripcion = inscripcionService.darDeBaja(id);
        redirectAttributes.addFlashAttribute("mensaje", "Se dio de baja a "
                + inscripcion.getSocio().getNombreCompleto() + " de " + inscripcion.getActividad().getNombre() + ".");
        if (volverA.equals("socio")) {
            return "redirect:/socios/" + inscripcion.getSocio().getId();
        }
        return "redirect:/actividades/" + inscripcion.getActividad().getId() + "/inscriptos";
    }

    private void prepararFormulario(Model model, Long socioId, Long actividadId) {
        List<Actividad> actividades = actividadService.listarTodas();
        model.addAttribute("socios", socioService.listarTodos());
        model.addAttribute("actividades", actividades);
        model.addAttribute("cupos", actividadService.calcularCuposDisponibles(actividades));
        model.addAttribute("socioId", socioId);
        model.addAttribute("actividadId", actividadId);
    }
}
