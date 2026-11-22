package com.losalgarrobos.socioclub.controller;

import com.losalgarrobos.socioclub.model.EstadoCuota;
import com.losalgarrobos.socioclub.model.Socio;
import com.losalgarrobos.socioclub.service.InscripcionService;
import com.losalgarrobos.socioclub.service.SocioService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

// Controlador MVC de la secretaría para los socios (RF5): listado paginado con búsqueda, alta y edición.
// Igual que ActividadController, solo habla con los servicios, nunca con los repositorios.
@Controller
@RequestMapping("/socios")
public class SocioController {

    private final SocioService socioService;
    private final InscripcionService inscripcionService;

    public SocioController(SocioService socioService, InscripcionService inscripcionService) {
        this.socioService = socioService;
        this.inscripcionService = inscripcionService;
    }

    // Listado paginado: /socios?apellido=gim&pagina=0
    // @RequestParam lee los parámetros de la URL; si no vienen, usa el valor por defecto
    @GetMapping
    public String listar(@RequestParam(defaultValue = "") String apellido,
                         @RequestParam(defaultValue = "0") int pagina,
                         Model model) {
        Page<Socio> paginaSocios = socioService.listar(apellido, Math.max(pagina, 0));
        model.addAttribute("pagina", paginaSocios);
        model.addAttribute("apellido", apellido);
        return "socios/lista";
    }

    // Ficha del socio con sus inscripciones activas
    @GetMapping("/{id}")
    public String detalle(@PathVariable Long id, Model model) {
        model.addAttribute("socio", socioService.buscarPorId(id));
        model.addAttribute("inscripciones", inscripcionService.listarActivasPorSocio(id));
        return "socios/detalle";
    }

    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        prepararFormulario(model, new Socio(), "Nuevo socio");
        return "socios/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        prepararFormulario(model, socioService.buscarPorId(id), "Editar socio");
        return "socios/formulario";
    }

    // Recibe el formulario (alta o modificación según tenga id o no)
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute("socio") Socio socio, Model model,
                          RedirectAttributes redirectAttributes) {
        try {
            socioService.guardar(socio);
            redirectAttributes.addFlashAttribute("mensaje",
                    "El socio " + socio.getNombreCompleto() + " se guardó correctamente.");
            return "redirect:/socios";
        } catch (IllegalArgumentException e) {
            // DNI inválido o repetido, fecha inválida: se vuelve al formulario con el error
            model.addAttribute("error", e.getMessage());
            prepararFormulario(model, socio, socio.getId() == null ? "Nuevo socio" : "Editar socio");
            return "socios/formulario";
        }
    }

    private void prepararFormulario(Model model, Socio socio, String titulo) {
        model.addAttribute("socio", socio);
        model.addAttribute("titulo", titulo);
        model.addAttribute("estados", EstadoCuota.values());
    }
}
