/*
  Esquema analítico opcional de SQL Server.
  Cargar aquí el CSV generado por la API antes de ejecutar informes.
  Este almacén es una réplica de lectura; PostgreSQL sigue siendo el origen.
*/
CREATE TABLE dbo.ResultadoExportado (
    ConvocatoriaId  varchar(36)   NOT NULL,
    Convocatoria    nvarchar(140) NOT NULL,
    ProyectoId      varchar(36)   NOT NULL,
    Proyecto        nvarchar(180) NOT NULL,
    Categoria       nvarchar(100) NOT NULL,
    Evaluaciones    int           NOT NULL,
    Promedio        decimal(6,2)  NULL,
    ExportadoEn     datetime2(0)  NOT NULL DEFAULT sysdatetime(),
    CONSTRAINT PK_ResultadoExportado PRIMARY KEY (ConvocatoriaId, ProyectoId),
    CONSTRAINT CK_ResultadoExportado_Evaluaciones CHECK (Evaluaciones >= 0)
);
GO

CREATE OR ALTER VIEW dbo.vw_ResumenPorCategoria
AS
    SELECT
        ConvocatoriaId,
        Convocatoria,
        Categoria,
        COUNT_BIG(*) AS Proyectos,
        SUM(Evaluaciones) AS Evaluaciones,
        CAST(AVG(Promedio) AS decimal(6,2)) AS PromedioCategoria
    FROM dbo.ResultadoExportado
    GROUP BY ConvocatoriaId, Convocatoria, Categoria;
GO

CREATE OR ALTER PROCEDURE dbo.usp_RankingConvocatoria
    @ConvocatoriaId varchar(36)
AS
BEGIN
    SET NOCOUNT ON;

    SELECT
        Proyecto,
        Categoria,
        Evaluaciones,
        Promedio,
        DENSE_RANK() OVER (ORDER BY Promedio DESC, Proyecto ASC) AS Posicion
    FROM dbo.ResultadoExportado
    WHERE ConvocatoriaId = @ConvocatoriaId
    ORDER BY Posicion, Proyecto;
END;
GO
