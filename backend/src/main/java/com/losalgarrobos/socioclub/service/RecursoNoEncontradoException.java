package com.losalgarrobos.socioclub.service;

// Se lanza cuando se busca un socio, actividad o inscripción que no existe (en la API será un 404)
public class RecursoNoEncontradoException extends RuntimeException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
