package com.losalgarrobos.socioclub.service;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.repository.ActividadRepository;
import com.losalgarrobos.socioclub.repository.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// @Service: Spring crea un único objeto (bean) de esta clase y lo inyecta donde se necesite
@Service
public class ActividadService {

    private final ActividadRepository actividadRepository;
    private final InscripcionRepository inscripcionRepository;

    // Inyección de dependencias por constructor: Spring pasa los repositorios automáticamente
    public ActividadService(ActividadRepository actividadRepository, InscripcionRepository inscripcionRepository) {
        this.actividadRepository = actividadRepository;
        this.inscripcionRepository = inscripcionRepository;
    }

    public List<Actividad> listarTodas() {
        return actividadRepository.findAllByOrderByNombreAsc();
    }

    public List<Actividad> listarPorDisciplina(String disciplina) {
        return actividadRepository.findByDisciplina(disciplina);
    }

    public Actividad buscarPorId(Long id) {
        return actividadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la actividad con id " + id));
    }

    public long contarInscriptosActivos(Long actividadId) {
        return inscripcionRepository.contarActivasPorActividad(actividadId);
    }

    // Cupo disponible = cupo máximo - inscripciones ACTIVAS
    public long calcularCupoDisponible(Actividad actividad) {
        return actividad.getCupoMaximo() - contarInscriptosActivos(actividad.getId());
    }

    // Devuelve un mapa idActividad -> cupo disponible, para mostrarlo en el listado
    public Map<Long, Long> calcularCuposDisponibles(List<Actividad> actividades) {
        Map<Long, Long> cupos = new HashMap<>();
        for (Actividad actividad : actividades) {
            cupos.put(actividad.getId(), calcularCupoDisponible(actividad));
        }
        return cupos;
    }

    @Transactional
    public Actividad guardar(Actividad actividad) {
        if (actividad.getEdadMinima() > actividad.getEdadMaxima()) {
            throw new IllegalArgumentException("La edad mínima no puede ser mayor que la edad máxima.");
        }
        if (actividad.getCupoMaximo() <= 0) {
            throw new IllegalArgumentException("El cupo máximo tiene que ser mayor que 0.");
        }
        if (actividad.getCuotaMensual() < 0) {
            throw new IllegalArgumentException("La cuota mensual no puede ser negativa.");
        }
        // Al editar, no se puede bajar el cupo por debajo de los inscriptos que ya tiene
        if (actividad.getId() != null) {
            long activos = contarInscriptosActivos(actividad.getId());
            if (actividad.getCupoMaximo() < activos) {
                throw new IllegalArgumentException("La actividad ya tiene " + activos
                        + " inscriptos activos: el cupo no puede ser menor.");
            }
        }
        return actividadRepository.save(actividad);
    }

    // Solo se puede eliminar una actividad sin inscriptos activos
    @Transactional
    public void eliminar(Long id) {
        Actividad actividad = buscarPorId(id);
        if (contarInscriptosActivos(id) > 0) {
            throw new ReglaNegocioException("ACTIVIDAD_CON_INSCRIPTOS",
                    "No se puede eliminar una actividad que tiene inscriptos activos.");
        }
        actividadRepository.delete(actividad);
    }
}
