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
import com.rosmar.incidencias.model.Estatus;
import java.util.List;

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
    public String listar(@RequestParam(required = false) String area,
                         @RequestParam(required = false) String estatus,
                         Model model) {
        List<Incidencia> incidencias = incidenciaService.listarTodas();

        if (area != null && !area.isEmpty()) {
            incidencias = incidencias.stream()
                    .filter(i -> i.getArea().getId().toString().equals(area))
                    .collect(java.util.stream.Collectors.toList());
        }
        if (estatus != null && !estatus.isEmpty()) {
            incidencias = incidencias.stream()
                    .filter(i -> i.getEstatus().name().equals(estatus))
                    .collect(java.util.stream.Collectors.toList());
        }

        model.addAttribute("incidencias", incidencias);
        model.addAttribute("areas", areaService.listarTodas());
        model.addAttribute("estatuses", Estatus.values());
        model.addAttribute("filtroArea", area);
        model.addAttribute("filtroEstatus", estatus);
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

    @PostMapping("/{id}/estatus")
    public String cambiarEstatus(@PathVariable Long id,
                                 @RequestParam Estatus estatus) {
        Incidencia incidencia = incidenciaService.buscarPorId(id).orElseThrow();
        incidencia.setEstatus(estatus);
        incidenciaService.guardar(incidencia);
        return "redirect:/incidencias";
    }
}