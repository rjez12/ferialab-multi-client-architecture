# FeriaLab

FeriaLab es una plataforma para publicar proyectos de una feria académica, coordinar su revisión y preparar informes. Este monorepo conecta una aplicación central con clientes JavaFX, WinForms, Tkinter y web, más un esquema opcional de análisis en SQL Server.

## Primera entrega

La primera versión ejecutable cubre el catálogo público: PostgreSQL guarda convocatorias y proyectos ficticios; una API Java/OpenXava los expone; los cuatro clientes permiten consultar y filtrar los proyectos; la herramienta Python puede exportarlos a CSV. El esquema T-SQL prepara un almacén de lectura para informes.

El flujo de asignación de jueces, rúbricas y puntuación es la siguiente etapa. Las pantallas actuales no guardan evaluaciones.

## Qué demuestra

- Un modelo relacional con UUID, claves foráneas, estados controlados e índices.
- Una API común consumida desde cuatro interfaces con lenguajes distintos.
- Carga HTTP asíncrona, búsqueda local, estados de conexión y exportación CSV.
- Separación entre la base transaccional PostgreSQL y los informes analíticos T-SQL.
- Una experiencia web propia y una aplicación administrativa generada desde entidades JPA/OpenXava.

## Tecnologías

| Tecnología | Uso en FeriaLab | Base en repositorios existentes |
| --- | --- | --- |
| Java 21, Maven, OpenXava, Spring MVC, JPA/Hibernate | Modelo del dominio, administración y API REST | [Testing-BFA](https://github.com/rjez12/Testing-BFA), [Javafx.Postgre](https://github.com/rjez12/Javafx.Postgre), [ConexionesDatabases](https://github.com/rjez12/ConexionesDatabases) |
| JavaFX | Catálogo de escritorio para revisión de proyectos | [Javafx.Postgre](https://github.com/rjez12/Javafx.Postgre), [ConexionesDatabases](https://github.com/rjez12/ConexionesDatabases) |
| PostgreSQL | Almacenamiento transaccional | [Javafx.Postgre](https://github.com/rjez12/Javafx.Postgre) |
| C# y Windows Forms (.NET Framework 4.7.2) | Panel de consulta para organización | [Innovatec](https://github.com/rjez12/Innovatec) |
| Python y Tkinter | Revisión y exportación de datos CSV | [sistemaEcuacionesLineales](https://github.com/rjez12/sistemaEcuacionesLineales) |
| HTML, CSS y JavaScript | Catálogo web adaptable | [Plataformas-de-Colaboracion-Digital](https://github.com/rjez12/Plataformas-de-Colaboracion-Digital) |
| T-SQL | Resumen por categoría y ranking analítico | [PracticaIntegral](https://github.com/rjez12/PracticaIntegral) |

## Ejecutar en local

Requisitos: Docker Desktop, JDK 21, Maven, Python 3 con Tkinter, y Windows para WinForms. JavaFX requiere conexión inicial a Maven Central para resolver dependencias.

1. Inicia PostgreSQL con `docker compose up -d postgres`. El contenedor ejecuta el esquema y los datos de ejemplo al crear su volumen por primera vez.
2. Desde `server/`, inicia el backend con `mvn spring-boot:run`.
3. Desde la raíz del repositorio, sirve `clients/web/` con `python -m http.server 4173 --directory clients/web`. Abre `http://127.0.0.1:4173`. La web consulta `http://localhost:8080/ferialab/api/v1` y muestra ejemplos si la API no responde.
4. JavaFX: ejecuta `mvn javafx:run` dentro de `clients/evaluator-javafx/`.
5. Python: ejecuta `python app.py` dentro de `clients/data-tools-python/`.
6. WinForms: abre `clients/organizer-winforms/FeriaLab.Organizer.csproj` en Visual Studio y ejecútalo.

Las aplicaciones Java, Python y WinForms aceptan la variable opcional `FERIALAB_API` para cambiar la base de la API. La configuración local de PostgreSQL se ajusta con `FERIALAB_DB_URL`, `FERIALAB_DB_USER` y `FERIALAB_DB_PASSWORD`. Docker usa credenciales de desarrollo solo para la máquina local.

## Estructura

```text
server/                         Java, OpenXava, Spring MVC y JPA
clients/web/                    HTML, CSS y JavaScript
clients/evaluator-javafx/       JavaFX
clients/organizer-winforms/     C# y Windows Forms
clients/data-tools-python/      Python, Tkinter y exportación CSV
database/postgresql/            Esquema operativo y datos ficticios
database/sqlserver/             Esquema y consultas T-SQL de lectura
docs/                           Arquitectura y contrato de API
```

## API disponible

Base local: `http://localhost:8080/ferialab/api/v1`.

- `GET /convocatorias`: convocatorias visibles.
- `GET /proyectos?publicados=true`: proyectos publicados.
- `GET /proyectos?convocatoriaId={uuid}`: filtra por convocatoria.
- `GET /proyectos?publicados=false`: consulta todos los estados para uso local.

La API pública solo devuelve campos de catálogo. La autenticación y los endpoints de escritura se implementarán junto con la asignación y evaluación; no se aceptan calificaciones en esta versión.

## Seguridad y datos

Los datos de ejemplo son inventados. No guardes claves ni datos reales de estudiantes en el repositorio. Los clientes hablan con la API y no reciben credenciales de PostgreSQL. El acceso de escritura requerirá autenticación antes de abrir la plataforma a una red pública.

## Referencias del proyecto

FeriaLab reúne áreas trabajadas en [Testing-BFA](https://github.com/rjez12/Testing-BFA), [Innovatec](https://github.com/rjez12/Innovatec), [sistemaEcuacionesLineales](https://github.com/rjez12/sistemaEcuacionesLineales), [Javafx.Postgre](https://github.com/rjez12/Javafx.Postgre), [ConexionesDatabases](https://github.com/rjez12/ConexionesDatabases), [PracticaIntegral](https://github.com/rjez12/PracticaIntegral) y [Plataformas-de-Colaboracion-Digital](https://github.com/rjez12/Plataformas-de-Colaboracion-Digital).
