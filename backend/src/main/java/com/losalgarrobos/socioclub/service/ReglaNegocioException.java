package com.losalgarrobos.socioclub.service;

// Se lanza cuando una regla de negocio (RN1 a RN5) rechaza una operación.
// El controlador Thymeleaf muestra el mensaje en la vista y el controlador REST
// la va a convertir en un 409 Conflict con {"error": mensaje, "regla": regla}.
public class ReglaNegocioException extends RuntimeException {

    private final String regla;

    public ReglaNegocioException(String regla, String mensaje) {
        super(mensaje);
        this.regla = regla;
    }

    public String getRegla() {
        return regla;
    }
}
