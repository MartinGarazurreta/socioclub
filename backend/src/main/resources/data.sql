-- =========================================================
-- Datos iniciales de SocioClub (sección 3.3 del enunciado)
-- Spring ejecuta este archivo cada vez que arranca, DESPUÉS de que Hibernate crea las tablas.
-- INSERT IGNORE: si la fila ya existe (mismo id), no la vuelve a insertar ni da error.
-- =========================================================

-- ---------- Actividades ----------
INSERT IGNORE INTO actividades (id, nombre, disciplina, dias, horario, edad_minima, edad_maxima, cupo_maximo, cuota_mensual, profesor) VALUES
(1, 'Fútbol infantil',   'Fútbol',    'Lun y Mié',      '18:00',  6, 12, 40, 18000, 'Diego Ferreyra'),
(2, 'Vóley mixto',       'Vóley',     'Mar y Jue',      '19:00', 13, 60, 24, 20000, 'Laura Ceballos'),
(3, 'Básquet juvenil',   'Básquet',   'Lun y Vie',      '19:30', 12, 17, 20, 19000, 'Martín Oviedo'),
(4, 'Natación',          'Natación',  'Mar y Jue',      '17:00',  5, 70, 30, 26000, 'Sofía Bustos'),
(5, 'Patín artístico',   'Patín',     'Mié y Vie',      '17:30',  5, 15, 25, 21000, 'Carla Peralta'),
(6, 'Ajedrez',           'Ajedrez',   'Sáb',            '10:00',  8, 99, 16,  9000, 'Raúl Giménez'),
(7, 'Funcional adultos', 'Fitness',   'Lun, Mié y Vie', '20:30', 18, 65, 30, 22000, 'Paula Luna'),
(8, 'Yoga',              'Bienestar', 'Mar y Jue',      '9:00',  16, 99, 15, 17000, 'Inés Arguello');

-- ---------- Socios de prueba (20, edades variadas, 2 MOROSOS: ids 5 y 12) ----------
INSERT IGNORE INTO socios (id, dni, nombre, apellido, fecha_nacimiento, email, telefono, estado_cuota) VALUES
( 1, '55123456', 'Tomás',     'Bustamante', '2018-03-15', 'familia.bustamante@mail.com', '3515550101', 'AL_DIA'),
( 2, '53987654', 'Valentina', 'Quiroga',    '2016-06-02', 'familia.quiroga@mail.com',    '3515550102', 'AL_DIA'),
( 3, '51456789', 'Mateo',     'Ledesma',    '2012-02-20', 'familia.ledesma@mail.com',    '3515550103', 'AL_DIA'),
( 4, '52345678', 'Martina',   'Funes',      '2014-09-10', 'familia.funes@mail.com',      '3515550104', 'AL_DIA'),
( 5, '50876543', 'Joaquín',   'Herrera',    '2011-05-30', 'familia.herrera@mail.com',    '3515550105', 'MOROSO'),
( 6, '56234567', 'Emma',      'Sosa',       '2020-01-12', 'familia.sosa@mail.com',       '3515550106', 'AL_DIA'),
( 7, '35123987', 'Lucía',     'Moyano',     '1990-04-11', 'lucia.moyano@mail.com',       '3515550107', 'AL_DIA'),
( 8, '31456123', 'Gustavo',   'Paredes',    '1985-07-23', 'gustavo.paredes@mail.com',    '3515550108', 'AL_DIA'),
( 9, '23789456', 'Claudia',   'Ríos',       '1972-11-02', 'claudia.rios@mail.com',       '3515550109', 'AL_DIA'),
(10, '41234765', 'Agustín',   'Carranza',   '1999-01-30', 'agustin.carranza@mail.com',   '3515550110', 'AL_DIA'),
(11, '43567890', 'Camila',    'Vera',       '2001-08-17', 'camila.vera@mail.com',        '3515550111', 'AL_DIA'),
(12, '20345678', 'Roberto',   'Agüero',     '1968-03-05', 'roberto.aguero@mail.com',     '3515550112', 'MOROSO'),
(13, '11987654', 'Norma',     'Castillo',   '1958-12-12', 'norma.castillo@mail.com',     '3515550113', 'AL_DIA'),
(14, '38765432', 'Federico',  'Luna',       '1995-10-01', 'federico.luna@mail.com',      '3515550114', 'AL_DIA'),
(15, '33654321', 'Sabrina',   'Ortiz',      '1988-02-14', 'sabrina.ortiz@mail.com',      '3515550115', 'AL_DIA'),
(16, '27543210', 'Marcelo',   'Peralta',    '1979-06-25', 'marcelo.peralta@mail.com',    '3515550116', 'AL_DIA'),
(17, '45678123', 'Julieta',   'Domínguez',  '2004-04-04', 'julieta.dominguez@mail.com',  '3515550117', 'AL_DIA'),
(18, '37890123', 'Pablo',     'Giménez',    '1993-09-19', 'pablo.gimenez@mail.com',      '3515550118', 'AL_DIA'),
(19, '14321987', 'Susana',    'Figueroa',   '1962-07-07', 'susana.figueroa@mail.com',    '3515550119', 'AL_DIA'),
(20, '49876321', 'Bruno',     'Acosta',     '2009-03-03', 'bruno.acosta@mail.com',       '3515550120', 'AL_DIA');

-- ---------- Inscripciones ----------
-- Yoga (cupo 15) queda con 14 inscriptos activos: CASI COMPLETA para demostrar RN1
-- La socia 7 (Lucía Moyano) tiene 3 actividades activas: sirve para demostrar RN5
INSERT IGNORE INTO inscripciones (id, fecha_inscripcion, estado, socio_id, actividad_id) VALUES
( 1, '2026-03-02', 'ACTIVA',  7, 8),
( 2, '2026-03-02', 'ACTIVA',  8, 8),
( 3, '2026-03-03', 'ACTIVA',  9, 8),
( 4, '2026-03-03', 'ACTIVA', 10, 8),
( 5, '2026-03-04', 'ACTIVA', 11, 8),
( 6, '2026-03-04', 'ACTIVA', 12, 8),
( 7, '2026-03-05', 'ACTIVA', 13, 8),
( 8, '2026-03-05', 'ACTIVA', 14, 8),
( 9, '2026-03-06', 'ACTIVA', 15, 8),
(10, '2026-03-06', 'ACTIVA', 16, 8),
(11, '2026-03-09', 'ACTIVA', 17, 8),
(12, '2026-03-09', 'ACTIVA', 18, 8),
(13, '2026-03-10', 'ACTIVA', 19, 8),
(14, '2026-03-10', 'ACTIVA', 20, 8),
(15, '2026-03-02', 'ACTIVA',  1, 1),
(16, '2026-03-02', 'ACTIVA',  2, 1),
(17, '2026-03-03', 'ACTIVA',  2, 4),
(18, '2026-03-03', 'ACTIVA',  3, 3),
(19, '2026-03-04', 'ACTIVA',  4, 5),
(20, '2026-03-04', 'ACTIVA',  5, 3),
(21, '2026-03-05', 'ACTIVA',  6, 4),
(22, '2026-03-05', 'ACTIVA',  7, 4),
(23, '2026-03-06', 'ACTIVA',  7, 7),
(24, '2026-03-06', 'ACTIVA',  8, 6),
(25, '2026-03-09', 'ACTIVA',  9, 2),
(26, '2026-03-09', 'ACTIVA', 10, 2),
(27, '2026-03-10', 'ACTIVA', 13, 6),
(28, '2026-04-01', 'BAJA',   11, 7);
