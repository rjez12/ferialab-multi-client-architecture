package org.ferialab.server.domain;

import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.openxava.annotations.Required;
import org.openxava.annotations.Tab;
import org.openxava.annotations.View;

@Entity
@Table(name = "proyecto")
@Tab(properties = "titulo, categoria, equipo, estado, convocatoria.nombre")
@View(members = "titulo; convocatoria; categoria; estado; resumen; equipo; urlRepositorio; creadoEn")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Required
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "convocatoria_id", nullable = false)
    private Convocatoria convocatoria;

    @Required
    @Column(name = "titulo", nullable = false, length = 180)
    private String titulo;

    @Required
    @Column(name = "resumen", nullable = false, length = 2400)
    private String resumen;

    @Required
    @Column(name = "categoria", nullable = false, length = 100)
    private String categoria;

    @Required
    @Column(name = "equipo", nullable = false, length = 180)
    private String equipo;

    @Column(name = "url_repositorio", length = 500)
    private String urlRepositorio;

    @Required
    @Column(nullable = false, length = 24)
    private String estado = "RECIBIDO";

    @Column(name = "creado_en", nullable = false, insertable = false, updatable = false)
    private Instant creadoEn;

    public UUID getId() { return id; }
    public Convocatoria getConvocatoria() { return convocatoria; }
    public void setConvocatoria(Convocatoria convocatoria) { this.convocatoria = convocatoria; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getEquipo() { return equipo; }
    public void setEquipo(String equipo) { this.equipo = equipo; }
    public String getUrlRepositorio() { return urlRepositorio; }
    public void setUrlRepositorio(String urlRepositorio) { this.urlRepositorio = urlRepositorio; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
    public Instant getCreadoEn() { return creadoEn; }
}
