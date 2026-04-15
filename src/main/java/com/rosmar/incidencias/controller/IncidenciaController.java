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
import jakarta.servlet.http.HttpServletResponse;

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
    @GetMapping("/reporte-pdf")
    public void generarPdf(@RequestParam(required = false) String area,
                           @RequestParam(required = false) String estatus,
                           HttpServletResponse response) throws Exception {

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

        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=reporte-incidencias.pdf");

        com.itextpdf.text.Document document = new com.itextpdf.text.Document();
        com.itextpdf.text.pdf.PdfWriter.getInstance(document, response.getOutputStream());
        document.open();

        // Título
        com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 16,
                com.itextpdf.text.Font.BOLD);
        document.add(new com.itextpdf.text.Paragraph(
                "Rosmar Consultoría de Sanidad", titleFont));
        document.add(new com.itextpdf.text.Paragraph(
                "Reporte de Incidencias Sanitarias"));
        document.add(new com.itextpdf.text.Paragraph(
                "Fecha de generación: " + java.time.LocalDate.now()));
        document.add(com.itextpdf.text.Chunk.NEWLINE);

        // Tabla
        com.itextpdf.text.pdf.PdfPTable table =
                new com.itextpdf.text.pdf.PdfPTable(6);
        table.setWidthPercentage(100);

        // Encabezados
        String[] headers = {"Folio", "Fecha", "Área", "Título", "Severidad", "Estatus"};
        for (String h : headers) {
            com.itextpdf.text.pdf.PdfPCell cell =
                    new com.itextpdf.text.pdf.PdfPCell(
                            new com.itextpdf.text.Phrase(h));
            cell.setBackgroundColor(new com.itextpdf.text.BaseColor(31, 56, 100));
            cell.setPadding(6);
            com.itextpdf.text.Font hFont = new com.itextpdf.text.Font(
                    com.itextpdf.text.Font.FontFamily.HELVETICA, 10,
                    com.itextpdf.text.Font.BOLD,
                    com.itextpdf.text.BaseColor.WHITE);
            cell.setPhrase(new com.itextpdf.text.Phrase(h, hFont));
            table.addCell(cell);
        }

        // Filas
        com.itextpdf.text.Font cellFont = new com.itextpdf.text.Font(
                com.itextpdf.text.Font.FontFamily.HELVETICA, 9);
        for (Incidencia inc : incidencias) {
            table.addCell(new com.itextpdf.text.Phrase(
                    inc.getId().toString(), cellFont));
            table.addCell(new com.itextpdf.text.Phrase(
                    inc.getFechaRegistro().toString(), cellFont));
            table.addCell(new com.itextpdf.text.Phrase(
                    inc.getArea().getNombre(), cellFont));
            table.addCell(new com.itextpdf.text.Phrase(
                    inc.getTitulo(), cellFont));
            table.addCell(new com.itextpdf.text.Phrase(
                    inc.getSeveridad().name(), cellFont));
            table.addCell(new com.itextpdf.text.Phrase(
                    inc.getEstatus().name(), cellFont));
        }

        document.add(table);
        document.close();
    }
}