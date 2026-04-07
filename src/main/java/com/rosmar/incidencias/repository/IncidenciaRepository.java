package com.rosmar.incidencias.repository;

import com.rosmar.incidencias.model.Area;
import com.rosmar.incidencias.model.Estatus;
import com.rosmar.incidencias.model.Incidencia;
import com.rosmar.incidencias.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IncidenciaRepository extends JpaRepository<Incidencia, Long> {

    List<Incidencia> findByEstatus(Estatus estatus);

    List<Incidencia> findByArea(Area area);

    List<Incidencia> findByReportadaPor(Usuario usuario);

    List<Incidencia> findByAsignadaA(Usuario usuario);
}
