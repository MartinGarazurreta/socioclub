package com.losalgarrobos.socioclub.repository;

import com.losalgarrobos.socioclub.model.Socio;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Long> {

    // Método derivado del nombre con paginación:
    // "SELECT ... FROM socios WHERE UPPER(apellido) LIKE UPPER('%texto%') LIMIT ? OFFSET ?"
    Page<Socio> findByApellidoContainingIgnoreCase(String apellido, Pageable pageable);

    // Método derivado del nombre: busca un socio por DNI (ingreso a la app de socios)
    Optional<Socio> findByDni(String dni);
}
