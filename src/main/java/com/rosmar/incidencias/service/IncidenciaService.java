package com.rosmar.incidencias.service;

import com.rosmar.incidencias.model.Estatus;
import com.rosmar.incidencias.model.Incidencia;
import com.rosmar.incidencias.repository.IncidenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IncidenciaService {

    private final IncidenciaRepository incidenciaRepository;

    public IncidenciaService(IncidenciaRepository incidenciaRepository) {
        this.incidenciaRepository = incidenciaRepository;
    }

    public List<Incidencia> listarTodas() {
        return incidenciaRepository.findAll();
    }

    public List<Incidencia> listarPorEstatus(Estatus estatus) {
        return incidenciaRepository.findByEstatus(estatus);
    }

    public Optional<Incidencia> buscarPorId(Long id) {
        return incidenciaRepository.findById(id);
    }

    public Incidencia guardar(Incidencia incidencia) {
        return incidenciaRepository.save(incidencia);
    }

    public void eliminar(Long id) {
        incidenciaRepository.deleteById(id);
    }
}
