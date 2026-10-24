package com.losalgarrobos.socioclub.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InicioController {

    // La pantalla inicial de la secretaría es el listado de actividades
    @GetMapping("/")
    public String inicio() {
        return "redirect:/actividades";
    }
}
