package com.losalgarrobos.socioclub.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDate;

// Entidad JPA: une un socio con una actividad. Es la tabla intermedia de la relación
// Socio <-> Actividad, pero con datos propios (fecha y estado).
@Entity
@Table(name = "inscripciones")
public class Inscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private EstadoInscripcion estado = EstadoInscripcion.ACTIVA;

    // Muchas inscripciones pertenecen a un socio: clave foránea socio_id
    @ManyToOne(optional = false)
    @JoinColumn(name = "socio_id")
    private Socio socio;

    // Muchas inscripciones pertenecen a una actividad: clave foránea actividad_id
    @ManyToOne(optional = false)
    @JoinColumn(name = "actividad_id")
    private Actividad actividad;

    public Inscripcion() {
    }

    public Inscripcion(Socio socio, Actividad actividad) {
        this.socio = socio;
        this.actividad = actividad;
        this.fechaInscripcion = LocalDate.now();
        this.estado = EstadoInscripcion.ACTIVA;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInscripcion() {
        return fechaInscripcion;
    }

    public void setFechaInscripcion(LocalDate fechaInscripcion) {
        this.fechaInscripcion = fechaInscripcion;
    }

    public EstadoInscripcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoInscripcion estado) {
        this.estado = estado;
    }

    public Socio getSocio() {
        return socio;
    }

    public void setSocio(Socio socio) {
        this.socio = socio;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public void setActividad(Actividad actividad) {
        this.actividad = actividad;
    }
}
