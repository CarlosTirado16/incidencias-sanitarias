package com.rosmar.incidencias.service;

import com.rosmar.incidencias.model.Area;
import com.rosmar.incidencias.repository.AreaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AreaService {

    private final AreaRepository areaRepository;

    public AreaService(AreaRepository areaRepository) {
        this.areaRepository = areaRepository;
    }

    public List<Area> listarTodas() {
        return areaRepository.findAll();
    }

    public Optional<Area> buscarPorId(Long id) {
        return areaRepository.findById(id);
    }

    public Area guardar(Area area) {
        return areaRepository.save(area);
    }

    public void eliminar(Long id) {
        areaRepository.deleteById(id);
    }
}
