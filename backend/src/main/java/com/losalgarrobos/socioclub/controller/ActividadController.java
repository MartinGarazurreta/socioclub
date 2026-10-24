package com.losalgarrobos.socioclub.controller;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.service.ActividadService;
import com.losalgarrobos.socioclub.service.ReglaNegocioException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

// Controlador MVC de la secretaría: recibe el pedido, llama al servicio y elige la vista Thymeleaf.
// No accede al repositorio ni tiene reglas de negocio.
@Controller
@RequestMapping("/actividades")
public class ActividadController {

    private final ActividadService actividadService;

    public ActividadController(ActividadService actividadService) {
        this.actividadService = actividadService;
    }

    // Listado con cupo disponible
    @GetMapping
    public String listar(Model model) {
        List<Actividad> actividades = actividadService.listarTodas();
        model.addAttribute("actividades", actividades);
        model.addAttribute("cupos", actividadService.calcularCuposDisponibles(actividades));
        return "actividades/lista";
    }

    // Formulario vacío para el alta
    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("actividad", new Actividad());
        model.addAttribute("titulo", "Nueva actividad");
        return "actividades/formulario";
    }

    // Formulario con los datos cargados para la edición
    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("actividad", actividadService.buscarPorId(id));
        model.addAttribute("titulo", "Editar actividad");
        return "actividades/formulario";
    }

    // Recibe el formulario (alta o modificación según tenga id o no)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("actividad") Actividad actividad, Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            actividadService.guardar(actividad);
            redirectAttributes.addFlashAttribute("mensaje", "La actividad " + actividad.getNombre() + " se guardó correctamente.");
            return "redirect:/actividades";
        } catch (IllegalArgumentException e) {
            // Si los datos no son válidos, se vuelve a mostrar el formulario con el error
            model.addAttribute("error", e.getMessage());
            model.addAttribute("titulo", actividad.getId() == null ? "Nueva actividad" : "Editar actividad");
            return "actividades/formulario";
        }
    }

    // Baja de la actividad (solo si no tiene inscriptos activos)
    @PostMapping("/{id}/eliminar")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            actividadService.eliminar(id);
            redirectAttributes.addFlashAttribute("mensaje", "La actividad se eliminó correctamente.");
        } catch (ReglaNegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/actividades";
    }
}
