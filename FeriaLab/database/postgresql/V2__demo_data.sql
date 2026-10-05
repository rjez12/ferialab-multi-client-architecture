-- Datos ficticios para probar FeriaLab localmente.
INSERT INTO convocatoria (id, nombre, periodo, estado, fecha_inicio, fecha_cierre)
VALUES ('d4102d62-b190-4d77-aead-43f82c68dc8a', 'Muestra de Innovación Universitaria', '2026', 'ABIERTA',
        '2026-09-01T00:00:00Z', '2026-11-30T23:59:59Z')
ON CONFLICT (id) DO NOTHING;

INSERT INTO proyecto (id, convocatoria_id, titulo, resumen, categoria, equipo, url_repositorio, estado, creado_en)
VALUES
('8da50e0e-719a-4401-9ddb-10db8fd6a203', 'd4102d62-b190-4d77-aead-43f82c68dc8a', 'Aula eficiente',
 'Un monitor para conocer y reducir el consumo eléctrico de los espacios de estudio.', 'Sostenibilidad', 'Colectivo Voltio', NULL, 'PUBLICADO', now() - interval '4 days'),
('b2ae7c01-e85b-4725-bbb8-2d7fbb7a43f7', 'd4102d62-b190-4d77-aead-43f82c68dc8a', 'Mapa de agua',
 'Registro colaborativo de puntos de agua y reportes de mantenimiento en el campus.', 'Tecnología', 'Equipo Nómada', NULL, 'PUBLICADO', now() - interval '3 days'),
('ff2ddcee-e4d4-4ee4-adc2-3290f0a07f75', 'd4102d62-b190-4d77-aead-43f82c68dc8a', 'Huerto cercano',
 'Una red local para compartir cosechas, semillas y talleres de cultivo urbano.', 'Comunidad', 'Semilla Urbana', NULL, 'PUBLICADO', now() - interval '2 days'),
('1a1e8248-0873-438e-bc14-f54f01ec823e', 'd4102d62-b190-4d77-aead-43f82c68dc8a', 'Turno claro',
 'Una propuesta para ordenar filas de atención y mostrar tiempos estimados.', 'Tecnología', 'Punto y Línea', NULL, 'PUBLICADO', now() - interval '1 day')
ON CONFLICT (id) DO NOTHING;
