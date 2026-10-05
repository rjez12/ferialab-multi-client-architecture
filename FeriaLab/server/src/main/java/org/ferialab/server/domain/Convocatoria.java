package org.ferialab.server.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;

@Entity
@Table(name = "convocatoria")
@Tab(properties = "nombre, periodo, estado, fechaInicio, fechaCierre")
@View(members = "nombre; periodo; estado; fechaInicio; fechaCierre")
public class Convocatoria {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Required
    @Column(name = "nombre", nullable = false, length = 140)
    private String nombre;

    @Required
    @Column(name = "periodo", nullable = false, length = 40)
    private String periodo;

    @Required
    @Column(nullable = false, length = 24)
    private String estado = "BORRADOR";

    @Column(name = "fecha_inicio")
    private Instant fechaInicio;

    @Column(name = "fecha_cierre")
    private Instant fechaCierre;

    public UUID getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getPeriodo() { return periodo; }
    public void setPeriodo(String periodo) { this.periodo = periodo; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Instant getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Instant fechaInicio) { this.fechaInicio = fechaInicio; }
    public Instant getFechaCierre() { return fechaCierre; }
    public void setFechaCierre(Instant fechaCierre) { this.fechaCierre = fechaCierre; }
}
