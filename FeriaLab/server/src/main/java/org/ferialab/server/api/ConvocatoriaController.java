package org.ferialab.server.api;

import jakarta.persistence.EntityManager;
import org.ferialab.server.domain.Convocatoria;
import org.openxava.jpa.XPersistence;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/convocatorias")
public class ConvocatoriaController {

    @GetMapping
    public List<ConvocatoriaPublica> listarConvocatorias() {
        EntityManager manager = XPersistence.createManager();
        try {
            return manager.createQuery(
                            "from Convocatoria c where c.estado in ('ABIERTA', 'EN_EVALUACION', 'CERRADA') order by c.fechaInicio desc",
                            Convocatoria.class)
                    .getResultList().stream()
                    .map(c -> new ConvocatoriaPublica(c.getId(), c.getNombre(), c.getPeriodo(), c.getEstado(),
                            c.getFechaInicio(), c.getFechaCierre()))
                    .toList();
        } finally {
            manager.close();
        }
    }

    public record ConvocatoriaPublica(UUID id, String nombre, String periodo, String estado,
                                      Instant fechaInicio, Instant fechaCierre) {}
}
