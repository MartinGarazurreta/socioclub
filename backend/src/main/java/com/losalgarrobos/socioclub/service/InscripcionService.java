package com.losalgarrobos.socioclub.service;

import com.losalgarrobos.socioclub.model.Actividad;
import com.losalgarrobos.socioclub.model.EstadoCuota;
import com.losalgarrobos.socioclub.model.EstadoInscripcion;
import com.losalgarrobos.socioclub.model.Inscripcion;
import com.losalgarrobos.socioclub.model.Socio;
import com.losalgarrobos.socioclub.repository.InscripcionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// Acá viven las reglas de negocio RN1 a RN5. Las usan tanto el controlador Thymeleaf
// (secretaría) como el controlador REST (app de socios): se escriben una sola vez.
@Service
public class InscripcionService {

    // RN5: si el docente pide cambiar el máximo de 3 a 4, se cambia solo este número
    public static final int MAXIMO_ACTIVIDADES = 3;

    private final InscripcionRepository inscripcionRepository;
    private final SocioService socioService;
    private final ActividadService actividadService;

    public InscripcionService(InscripcionRepository inscripcionRepository,
                              SocioService socioService,
                              ActividadService actividadService) {
        this.inscripcionRepository = inscripcionRepository;
        this.socioService = socioService;
        this.actividadService = actividadService;
    }

    @Transactional
    public Inscripcion inscribir(Long socioId, Long actividadId) {
        Socio socio = socioService.buscarPorId(socioId);
        Actividad actividad = actividadService.buscarPorId(actividadId);

        // RN4: cuota al día
        if (socio.getEstadoCuota() == EstadoCuota.MOROSO) {
            throw new ReglaNegocioException("RN4", "Debe regularizar su cuota en secretaría");
        }

        // RN2: sin duplicados
        if (inscripcionRepository.existsBySocioIdAndActividadIdAndEstado(
                socioId, actividadId, EstadoInscripcion.ACTIVA)) {
            throw new ReglaNegocioException("RN2", "El socio ya está inscripto en esta actividad");
        }

        // RN5: máximo de actividades
        long activasDelSocio = inscripcionRepository.countBySocioIdAndEstado(socioId, EstadoInscripcion.ACTIVA);
        if (activasDelSocio >= MAXIMO_ACTIVIDADES) {
            throw new ReglaNegocioException("RN5", "Alcanzó el máximo de " + MAXIMO_ACTIVIDADES + " actividades");
        }

        // RN3: edad dentro del rango de la actividad
        int edad = socio.getEdad();
        if (!actividad.esAptaParaEdad(edad)) {
            throw new ReglaNegocioException("RN3", "La edad del socio (" + edad + " años) está fuera del rango "
                    + "permitido para " + actividad.getNombre() + ": de " + actividad.getEdadMinima()
                    + " a " + actividad.getEdadMaxima() + " años");
        }

        // RN1: cupo disponible
        if (actividadService.calcularCupoDisponible(actividad) <= 0) {
            throw new ReglaNegocioException("RN1", "La actividad no tiene cupo disponible");
        }

        return inscripcionRepository.save(new Inscripcion(socio, actividad));
    }

    // RF9: baja lógica, la inscripción no se borra, cambia a estado BAJA
    @Transactional
    public Inscripcion darDeBaja(Long inscripcionId) {
        Inscripcion inscripcion = inscripcionRepository.findById(inscripcionId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la inscripción con id " + inscripcionId));
        inscripcion.setEstado(EstadoInscripcion.BAJA);
        return inscripcionRepository.save(inscripcion);
    }

    public List<Inscripcion> listarActivasPorActividad(Long actividadId) {
        return inscripcionRepository.findByActividadIdAndEstado(actividadId, EstadoInscripcion.ACTIVA);
    }

    public List<Inscripcion> listarActivasPorSocio(Long socioId) {
        socioService.buscarPorId(socioId); // si el socio no existe, lanza 404
        return inscripcionRepository.findBySocioIdAndEstado(socioId, EstadoInscripcion.ACTIVA);
    }
}
