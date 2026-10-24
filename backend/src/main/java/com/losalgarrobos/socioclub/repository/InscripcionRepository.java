package com.losalgarrobos.socioclub.repository;

import com.losalgarrobos.socioclub.model.EstadoInscripcion;
import com.losalgarrobos.socioclub.model.Inscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {

    // Consulta escrita a mano con @Query (en JPQL: se usan clases y atributos, no tablas)
    // Cuenta las inscripciones ACTIVAS de una actividad (se usa para RN1 y para el cupo disponible)
    @Query("SELECT COUNT(i) FROM Inscripcion i "
            + "WHERE i.actividad.id = :actividadId "
            + "AND i.estado = com.losalgarrobos.socioclub.model.EstadoInscripcion.ACTIVA")
    long contarActivasPorActividad(@Param("actividadId") Long actividadId);

    // RN5: cuántas inscripciones en cierto estado tiene un socio
    long countBySocioIdAndEstado(Long socioId, EstadoInscripcion estado);

    // RN2: si el socio ya tiene una inscripción en ese estado para esa actividad
    boolean existsBySocioIdAndActividadIdAndEstado(Long socioId, Long actividadId, EstadoInscripcion estado);

    // Inscriptos de una actividad (RF7)
    List<Inscripcion> findByActividadIdAndEstado(Long actividadId, EstadoInscripcion estado);

    // Inscripciones de un socio (RF8)
    List<Inscripcion> findBySocioIdAndEstado(Long socioId, EstadoInscripcion estado);
}
