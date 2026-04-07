package com.rosmar.incidencias.repository;

import com.rosmar.incidencias.model.Area;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AreaRepository extends JpaRepository<Area, Long> {

    boolean existsByNombre(String nombre);
}
