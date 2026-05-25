-- SCRIPT 08: DATOS DE MEDICOS PARA UNA BD EXISTENTE
-- Ejecutar como: app_citas

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
)
SELECT
    'CC', '1001001001', 'LAURA', 'ANDREA',
    'GOMEZ', 'MORALES', DATE '1982-04-15', 'F',
    'laura.gomez@clinica.com', '3001234567',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '11001')
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001001'
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
)
SELECT
    'CC', '1001001002', 'CARLOS', 'ENRIQUE',
    'RAMIREZ', 'TORRES', DATE '1978-09-22', 'M',
    'carlos.ramirez@clinica.com', '3002345678',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '05001')
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001002'
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
)
SELECT
    'CC', '1001001003', 'MARIANA', NULL,
    'PEREZ', 'CASTILLO', DATE '1987-01-30', 'F',
    'mariana.perez@clinica.com', '3003456789',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '76001')
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001003'
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
)
SELECT
    'CC', '1001001004', 'JUAN', 'PABLO',
    'MARTINEZ', 'ROJAS', DATE '1980-12-05', 'M',
    'juan.martinez@clinica.com', '3004567890',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '08001')
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001004'
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
)
SELECT
    'CC', '1001001005', 'PAULA', 'SOFIA',
    'HERNANDEZ', 'LOPEZ', DATE '1985-06-18', 'F',
    'paula.hernandez@clinica.com', '3005678901',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '68001')
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001005'
);

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
SELECT
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001001'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'MG'),
    'RM-0001',
    85000
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicos WHERE numero_registro = 'RM-0001');

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
SELECT
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001002'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'CARD'),
    'RM-0002',
    150000
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicos WHERE numero_registro = 'RM-0002');

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
SELECT
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001003'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'PED'),
    'RM-0003',
    110000
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicos WHERE numero_registro = 'RM-0003');

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
SELECT
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001004'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'GIN'),
    'RM-0004',
    130000
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicos WHERE numero_registro = 'RM-0004');

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
SELECT
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001005'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'ORT'),
    'RM-0005',
    140000
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM medicos WHERE numero_registro = 'RM-0005');

COMMIT;

SELECT 'MEDICOS' tabla, COUNT(*) registros FROM medicos;
