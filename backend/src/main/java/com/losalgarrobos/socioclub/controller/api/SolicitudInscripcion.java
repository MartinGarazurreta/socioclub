package com.losalgarrobos.socioclub.controller.api;

// Cuerpo del POST /api/inscripciones: { "socioId": 3, "actividadId": 1 }
// Spring (Jackson) convierte el JSON a este objeto usando los setters.
public class SolicitudInscripcion {

    private Long socioId;
    private Long actividadId;

    public Long getSocioId() {
        return socioId;
    }

    public void setSocioId(Long socioId) {
        this.socioId = socioId;
    }

    public Long getActividadId() {
        return actividadId;
    }

    public void setActividadId(Long actividadId) {
        this.actividadId = actividadId;
    }
}
