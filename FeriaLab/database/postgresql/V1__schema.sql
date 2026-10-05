-- Esquema inicial de FeriaLab. La migración se ejecuta contra PostgreSQL.
CREATE TABLE convocatoria (
    id              UUID PRIMARY KEY,
    nombre          VARCHAR(140) NOT NULL,
    periodo         VARCHAR(40) NOT NULL,
    estado          VARCHAR(24) NOT NULL DEFAULT 'BORRADOR'
                    CHECK (estado IN ('BORRADOR', 'ABIERTA', 'EN_EVALUACION', 'CERRADA')),
    fecha_inicio    TIMESTAMPTZ,
    fecha_cierre    TIMESTAMPTZ,
    creada_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_convocatoria_fechas CHECK (
        fecha_inicio IS NULL OR fecha_cierre IS NULL OR fecha_cierre > fecha_inicio
    )
);

CREATE TABLE proyecto (
    id              UUID PRIMARY KEY,
    convocatoria_id UUID NOT NULL REFERENCES convocatoria(id),
    titulo          VARCHAR(180) NOT NULL,
    resumen         VARCHAR(2400) NOT NULL,
    categoria       VARCHAR(100) NOT NULL,
    equipo          VARCHAR(180) NOT NULL,
    url_repositorio VARCHAR(500),
    estado          VARCHAR(24) NOT NULL DEFAULT 'RECIBIDO'
                    CHECK (estado IN ('RECIBIDO', 'EN_REVISION', 'EVALUADO', 'PUBLICADO')),
    creado_en       TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_proyecto_titulo_convocatoria UNIQUE (convocatoria_id, titulo)
);

CREATE TABLE evaluador (
    id              UUID PRIMARY KEY,
    nombre          VARCHAR(140) NOT NULL,
    correo          VARCHAR(254) NOT NULL UNIQUE,
    activo          BOOLEAN NOT NULL DEFAULT true
);

CREATE TABLE criterio (
    id              UUID PRIMARY KEY,
    convocatoria_id UUID NOT NULL REFERENCES convocatoria(id),
    nombre          VARCHAR(120) NOT NULL,
    descripcion     VARCHAR(700) NOT NULL DEFAULT '',
    peso            NUMERIC(5,2) NOT NULL CHECK (peso > 0 AND peso <= 100),
    nota_minima     NUMERIC(5,2) NOT NULL DEFAULT 0,
    nota_maxima     NUMERIC(5,2) NOT NULL DEFAULT 5,
    orden           SMALLINT NOT NULL DEFAULT 0,
    CONSTRAINT ck_criterio_rango CHECK (nota_maxima > nota_minima),
    CONSTRAINT uq_criterio_convocatoria UNIQUE (convocatoria_id, nombre)
);

CREATE TABLE asignacion (
    id              UUID PRIMARY KEY,
    proyecto_id     UUID NOT NULL REFERENCES proyecto(id),
    evaluador_id    UUID NOT NULL REFERENCES evaluador(id),
    asignada_en     TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_asignacion UNIQUE (proyecto_id, evaluador_id)
);

CREATE TABLE evaluacion (
    id              UUID PRIMARY KEY,
    asignacion_id   UUID NOT NULL UNIQUE REFERENCES asignacion(id),
    estado          VARCHAR(20) NOT NULL DEFAULT 'PENDIENTE'
                    CHECK (estado IN ('PENDIENTE', 'EN_PROGRESO', 'COMPLETADA')),
    enviada_en      TIMESTAMPTZ,
    creada_en       TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE TABLE puntuacion (
    id              UUID PRIMARY KEY,
    evaluacion_id   UUID NOT NULL REFERENCES evaluacion(id) ON DELETE CASCADE,
    criterio_id     UUID NOT NULL REFERENCES criterio(id),
    nota            NUMERIC(5,2) NOT NULL CHECK (nota >= 0 AND nota <= 100),
    comentario      VARCHAR(1200) NOT NULL DEFAULT '',
    actualizada_en  TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT uq_puntuacion_evaluacion_criterio UNIQUE (evaluacion_id, criterio_id)
);

CREATE INDEX ix_proyecto_convocatoria_estado ON proyecto (convocatoria_id, estado);
CREATE INDEX ix_asignacion_evaluador ON asignacion (evaluador_id);
CREATE INDEX ix_evaluacion_estado ON evaluacion (estado);

-- El total de pesos se valida al activar una rúbrica en la capa de dominio.
