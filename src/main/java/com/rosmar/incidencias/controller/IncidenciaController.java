package com.rosmar.incidencias.controller;

import com.rosmar.incidencias.model.Area;
import com.rosmar.incidencias.model.Incidencia;
import com.rosmar.incidencias.model.Severidad;
import com.rosmar.incidencias.model.Usuario;
import com.rosmar.incidencias.service.AreaService;
import com.rosmar.incidencias.service.IncidenciaService;
import com.rosmar.incidencias.service.UsuarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/incidencias")
public class IncidenciaController {

    private final IncidenciaService incidenciaService;
    private final AreaService areaService;
    private final UsuarioService usuarioService;

    public IncidenciaController(IncidenciaService incidenciaService,
                                AreaService areaService,
                                UsuarioService usuarioService) {
        this.incidenciaService = incidenciaService;
        this.areaService = areaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("incidencias", incidenciaService.listarTodas());
        model.addAttribute("areas", areaService.listarTodas());
        return "incidencias/lista";
    }

    @GetMapping("/nueva")
    public String nueva(Model model) {
        model.addAttribute("incidencia", new Incidencia());
        model.addAttribute("areas", areaService.listarTodas());
        model.addAttribute("severidades", Severidad.values());
        return "incidencias/nueva";
    }

    @PostMapping("/nueva")
    public String guardar(@ModelAttribute Incidencia incidencia,
                          @RequestParam Long areaId,
                          Principal principal) {
        Usuario usuario = usuarioService.buscarPorEmail(principal.getName())
                .orElseThrow();
        Area area = areaService.buscarPorId(areaId).orElseThrow();
        incidencia.setArea(area);
        incidencia.setReportadaPor(usuario);
        incidenciaService.guardar(incidencia);
        return "redirect:/incidencias";
    }
}