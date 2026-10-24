package com.losalgarrobos.socioclub.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

// Entidad JPA: Hibernate la traduce a la tabla "actividades"
@Entity
@Table(name = "actividades")
public class Actividad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 60)
    private String nombre;

    @Column(nullable = false, length = 40)
    private String disciplina;

    @Column(nullable = false, length = 60)
    private String profesor;

    @Column(nullable = false, length = 40)
    private String dias;

    @Column(nullable = false, length = 10)
    private String horario;

    @Column(name = "edad_minima", nullable = false)
    private Integer edadMinima;

    @Column(name = "edad_maxima", nullable = false)
    private Integer edadMaxima;

    @Column(name = "cupo_maximo", nullable = false)
    private Integer cupoMaximo;

    @Column(name = "cuota_mensual", nullable = false)
    private Double cuotaMensual;

    // Una actividad tiene muchas inscripciones. Al eliminar la actividad se eliminan también
    // sus inscripciones dadas de BAJA (el servicio no deja eliminarla si tiene inscripciones ACTIVAS).
    @OneToMany(mappedBy = "actividad", cascade = CascadeType.REMOVE)
    private List<Inscripcion> inscripciones = new ArrayList<>();

    public Actividad() {
    }

    public boolean esAptaParaEdad(int edad) {
        return edad >= edadMinima && edad <= edadMaxima;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }

    public String getProfesor() {
        return profesor;
    }

    public void setProfesor(String profesor) {
        this.profesor = profesor;
    }

    public String getDias() {
        return dias;
    }

    public void setDias(String dias) {
        this.dias = dias;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Integer getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(Integer edadMinima) {
        this.edadMinima = edadMinima;
    }

    public Integer getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(Integer edadMaxima) {
        this.edadMaxima = edadMaxima;
    }

    public Integer getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(Integer cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public Double getCuotaMensual() {
        return cuotaMensual;
    }

    public void setCuotaMensual(Double cuotaMensual) {
        this.cuotaMensual = cuotaMensual;
    }

    public List<Inscripcion> getInscripciones() {
        return inscripciones;
    }

    public void setInscripciones(List<Inscripcion> inscripciones) {
        this.inscripciones = inscripciones;
    }
}
