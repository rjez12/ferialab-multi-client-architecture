# API de FeriaLab

Base local: `http://localhost:8080/ferialab/api/v1`. La primera entrega publica endpoints de solo lectura para alimentar los clientes.

## Endpoints implementados

| Método | Ruta | Respuesta |
| --- | --- | --- |
| `GET` | `/convocatorias` | Convocatorias en estado ABIERTA, EN_EVALUACION o CERRADA. |
| `GET` | `/proyectos?publicados=true` | Proyectos con estado PUBLICADO; es el valor predeterminado. |
| `GET` | `/proyectos?convocatoriaId={uuid}` | Proyectos de una convocatoria concreta. |
| `GET` | `/proyectos?publicados=false` | Todos los proyectos, para uso local. |

Los proyectos se ordenan por fecha de creación descendente. El DTO público contiene `id`, `title`, `category`, `summary`, `team`, `stage` y `urlRepositorio`. No expone datos de evaluadores ni comentarios privados.

Ejemplo de respuesta:

```json
[
  {
    "id": "8da50e0e-719a-4401-9ddb-10db8fd6a203",
    "title": "Aula eficiente",
    "category": "Sostenibilidad",
    "summary": "Un monitor para conocer y reducir el consumo eléctrico.",
    "team": "Colectivo Voltio",
    "stage": "PUBLICADO",
    "urlRepositorio": "https://github.com/"
  }
]
```

## Próximos recursos

Estos recursos pertenecen a etapas siguientes y todavía no están implementados:

| Método | Ruta | Uso previsto |
| --- | --- | --- |
| `POST` | `/proyectos` | Registrar un proyecto con acceso autenticado. |
| `GET` | `/evaluaciones/pendientes` | Consultar la cola del evaluador actual. |
| `PUT` | `/evaluaciones/{id}/puntuaciones` | Guardar puntuaciones y comentarios. |
| `GET` | `/reportes/convocatorias/{id}/export.csv` | Exportar resultados autorizados. |

Los endpoints de escritura requieren autenticación, autorización por rol y validación de dominio antes de exponerse.
