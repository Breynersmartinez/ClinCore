-- SCRIPT 09: DATOS DE PACIENTES Y CITAS PARA UNA BD EXISTENTE
-- Ejecutar como: app_citas

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
)
SELECT
    'CC', '2002002001', 'ANA', 'MARIA',
    'RODRIGUEZ', 'SILVA', DATE '1990-03-12', 'F',
    'ana.rodriguez@email.com', '3101234567',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '11001')
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '2002002001'
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
)
SELECT
    'CC', '2002002002', 'MIGUEL', 'ANGEL',
    'SANCHEZ', 'DIAZ', DATE '1988-07-09', 'M',
    'miguel.sanchez@email.com', '3102345678',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '05001')
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '2002002002'
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
)
SELECT
    'CC', '2002002003', 'CAMILA', NULL,
    'VARGAS', 'MEJIA', DATE '1995-11-25', 'F',
    'camila.vargas@email.com', '3103456789',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '76001')
FROM dual
WHERE NOT EXISTS (
    SELECT 1 FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '2002002003'
);

INSERT INTO pacientes (id_persona, numero_historia, tipo_afiliacion, eps)
SELECT
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '2002002001'),
    'HC-0001',
    'CONTRIBUTIVO',
    'SANITAS'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM pacientes WHERE numero_historia = 'HC-0001');

INSERT INTO pacientes (id_persona, numero_historia, tipo_afiliacion, eps)
SELECT
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '2002002002'),
    'HC-0002',
    'SUBSIDIADO',
    'NUEVA EPS'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM pacientes WHERE numero_historia = 'HC-0002');

INSERT INTO pacientes (id_persona, numero_historia, tipo_afiliacion, eps)
SELECT
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '2002002003'),
    'HC-0003',
    'PARTICULAR',
    NULL
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM pacientes WHERE numero_historia = 'HC-0003');

INSERT INTO citas_medicas (
    numero_cita, id_paciente, id_medico, id_consultorio,
    fecha_cita, hora_inicio, hora_fin, estado, motivo_consulta
)
SELECT
    'CITA-SEED-0001',
    (SELECT id_paciente FROM pacientes WHERE numero_historia = 'HC-0001'),
    (SELECT id_medico FROM medicos WHERE numero_registro = 'RM-0001'),
    (SELECT id_consultorio FROM consultorios WHERE codigo_consultorio = 'CON001' AND id_sede = 1),
    TRUNC(SYSDATE) + 1,
    '08:00',
    '08:30',
    'PROGRAMADA',
    'Consulta general'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM citas_medicas WHERE numero_cita = 'CITA-SEED-0001');

INSERT INTO citas_medicas (
    numero_cita, id_paciente, id_medico, id_consultorio,
    fecha_cita, hora_inicio, hora_fin, estado, motivo_consulta
)
SELECT
    'CITA-SEED-0002',
    (SELECT id_paciente FROM pacientes WHERE numero_historia = 'HC-0002'),
    (SELECT id_medico FROM medicos WHERE numero_registro = 'RM-0002'),
    (SELECT id_consultorio FROM consultorios WHERE codigo_consultorio = 'CON002' AND id_sede = 1),
    TRUNC(SYSDATE) + 1,
    '09:00',
    '09:30',
    'CONFIRMADA',
    'Control cardiologia'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM citas_medicas WHERE numero_cita = 'CITA-SEED-0002');

INSERT INTO citas_medicas (
    numero_cita, id_paciente, id_medico, id_consultorio,
    fecha_cita, hora_inicio, hora_fin, estado, motivo_consulta
)
SELECT
    'CITA-SEED-0003',
    (SELECT id_paciente FROM pacientes WHERE numero_historia = 'HC-0003'),
    (SELECT id_medico FROM medicos WHERE numero_registro = 'RM-0003'),
    (SELECT id_consultorio FROM consultorios WHERE codigo_consultorio = 'CON003' AND id_sede = 1),
    TRUNC(SYSDATE) + 2,
    '10:00',
    '10:30',
    'PROGRAMADA',
    'Valoracion pediatrica'
FROM dual
WHERE NOT EXISTS (SELECT 1 FROM citas_medicas WHERE numero_cita = 'CITA-SEED-0003');

COMMIT;

SELECT 'PACIENTES' tabla, COUNT(*) registros FROM pacientes
UNION ALL
SELECT 'CITAS_MEDICAS', COUNT(*) FROM citas_medicas;
