package com.losalgarrobos.socioclub.repository;

import com.losalgarrobos.socioclub.model.Actividad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// JpaRepository ya trae save, findById, findAll, deleteById, count, etc.
public interface ActividadRepository extends JpaRepository<Actividad, Long> {

    // Método derivado del nombre: Spring arma "SELECT ... FROM actividades WHERE disciplina = ?"
    List<Actividad> findByDisciplina(String disciplina);

    // Método derivado del nombre: "SELECT ... FROM actividades ORDER BY nombre ASC"
    List<Actividad> findAllByOrderByNombreAsc();
}
