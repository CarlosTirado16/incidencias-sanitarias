package com.rosmar.incidencias.controller;

import com.rosmar.incidencias.service.IncidenciaService;
import com.rosmar.incidencias.service.AreaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;
    private final AreaService areaService;

    public IncidenciaController(IncidenciaService incidenciaService, AreaService areaService) {
        this.incidenciaService = incidenciaService;
        this.areaService = areaService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("incidencias", incidenciaService.listarTodas());
        model.addAttribute("areas", areaService.listarTodas());
        return "incidencias/lista";
    }
}