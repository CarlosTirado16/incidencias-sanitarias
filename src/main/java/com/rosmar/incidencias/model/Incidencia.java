package com.rosmar.incidencias.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "incidencias")
public class Incidencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Severidad severidad;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private Estatus estatus;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @Column
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "area_id", nullable = false)
    private Area area;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reportada_por_id", nullable = false)
    private Usuario reportadaPor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignada_a_id")
    private Usuario asignadaA;

    @PrePersist
    protected void alPersistir() {
        this.fechaRegistro = LocalDateTime.now();
        this.estatus = Estatus.ABIERTA;
    }

    @PreUpdate
    protected void alActualizar() {
        this.fechaActualizacion = LocalDateTime.now();
    }

    public Incidencia() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Severidad getSeveridad() { return severidad; }
    public void setSeveridad(Severidad severidad) { this.severidad = severidad; }

    public Estatus getEstatus() { return estatus; }
    public void setEstatus(Estatus estatus) { this.estatus = estatus; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }

    public LocalDateTime getFechaActualizacion() { return fechaActualizacion; }

    public Area getArea() { return area; }
    public void setArea(Area area) { this.area = area; }

    public Usuario getReportadaPor() { return reportadaPor; }
    public void setReportadaPor(Usuario reportadaPor) { this.reportadaPor = reportadaPor; }

    public Usuario getAsignadaA() { return asignadaA; }
    public void setAsignadaA(Usuario asignadaA) { this.asignadaA = asignadaA; }
}
