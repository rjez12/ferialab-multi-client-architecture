package org.ferialab.server.api;

import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.ferialab.server.domain.Proyecto;
import org.openxava.jpa.XPersistence;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProyectoController {

    @GetMapping("/proyectos")
    public List<ProyectoPublico> listarProyectos(
            @RequestParam(name = "publicados", defaultValue = "true") boolean publicados,
            @RequestParam(name = "convocatoriaId", required = false) UUID convocatoriaId) {
        EntityManager manager = XPersistence.createManager();
        try {
            StringBuilder queryText = new StringBuilder("from Proyecto p where 1 = 1");
            if (publicados) queryText.append(" and p.estado = :estado");
            if (convocatoriaId != null) queryText.append(" and p.convocatoria.id = :convocatoriaId");
            queryText.append(" order by p.creadoEn desc");
            TypedQuery<Proyecto> query = manager.createQuery(queryText.toString(), Proyecto.class);
            if (publicados) query.setParameter("estado", "PUBLICADO");
            if (convocatoriaId != null) query.setParameter("convocatoriaId", convocatoriaId);
            return query.getResultList().stream().map(ProyectoController::toPublicDto).toList();
        } finally {
            manager.close();
        }
    }

    private static ProyectoPublico toPublicDto(Proyecto project) {
        return new ProyectoPublico(
                project.getId(),
                project.getTitulo(),
                project.getCategoria(),
                project.getResumen(),
                project.getEquipo(),
                project.getEstado(),
                project.getUrlRepositorio());
    }

    public record ProyectoPublico(
            UUID id,
            String title,
            String category,
            String summary,
            String team,
            String stage,
            String urlRepositorio) {}
}
