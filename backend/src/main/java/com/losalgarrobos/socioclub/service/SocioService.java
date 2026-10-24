package com.losalgarrobos.socioclub.service;

import com.losalgarrobos.socioclub.model.Socio;
import com.losalgarrobos.socioclub.repository.SocioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SocioService {

    public static final int SOCIOS_POR_PAGINA = 10;

    private final SocioRepository socioRepository;

    public SocioService(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    // Listado paginado y ordenado por apellido; si apellido viene vacío trae todos
    public Page<Socio> listar(String apellido, int pagina) {
        Pageable pageable = PageRequest.of(pagina, SOCIOS_POR_PAGINA, Sort.by("apellido", "nombre"));
        String texto = (apellido == null) ? "" : apellido.trim();
        return socioRepository.findByApellidoContainingIgnoreCase(texto, pageable);
    }

    public List<Socio> listarTodos() {
        return socioRepository.findAll(Sort.by("apellido", "nombre"));
    }

    public Socio buscarPorId(Long id) {
        return socioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el socio con id " + id));
    }

    public Socio buscarPorDni(String dni) {
        return socioRepository.findByDni(dni)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe un socio con DNI " + dni));
    }

    @Transactional
    public Socio guardar(Socio socio) {
        // RN7 también del lado del servidor: 7 u 8 dígitos, sin puntos
        if (socio.getDni() == null || !socio.getDni().matches("\\d{7,8}")) {
            throw new IllegalArgumentException("El DNI debe tener 7 u 8 números, sin puntos.");
        }
        Optional<Socio> otro = socioRepository.findByDni(socio.getDni());
        if (otro.isPresent() && !otro.get().getId().equals(socio.getId())) {
            throw new IllegalArgumentException("Ya existe un socio con el DNI " + socio.getDni() + ".");
        }
        return socioRepository.save(socio);
    }
}
