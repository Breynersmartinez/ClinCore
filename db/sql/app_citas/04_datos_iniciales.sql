
-- SCRIPT 04: DATOS INICIALES (SEED DATA)
-- Sistema de Gestión de Citas Médicas
-- Oracle XE 18c - Esquema: APP_CITAS

-- Ejecutar como: app_citas



-- DEPARTAMENTOS (10 registros)

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('05', 'ANTIOQUIA');

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('11', 'BOGOTÁ D.C.');

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('08', 'ATLÁNTICO');

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('13', 'BOLÍVAR');

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('17', 'CALDAS');

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('19', 'CAUCA');

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('27', 'CHOCÓ');

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('68', 'SANTANDER');

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('76', 'VALLE DEL CAUCA');

INSERT INTO departamentos (codigo_dane, nombre_departamento)
VALUES ('73', 'TOLIMA');


-- MUNICIPIOS (10 registros)

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('05001', 'MEDELLÍN', 1);

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('11001', 'BOGOTÁ D.C.', 2);

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('08001', 'BARRANQUILLA', 3);

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('13001', 'CARTAGENA DE INDIAS', 4);

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('17001', 'MANIZALES', 5);

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('19001', 'POPAYÁN', 6);

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('68001', 'BUCARAMANGA', 8);

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('76001', 'CALI', 9);

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('73001', 'IBAGUÉ', 10);

INSERT INTO municipios (codigo_dane, nombre_municipio, id_departamento)
VALUES ('05002', 'ABEJORRAL', 1);


-- SEDES (6 registros)

INSERT INTO sedes (codigo_sede, nombre_sede, direccion, telefono, email, id_municipio)
VALUES ('SED001', 'SEDE PRINCIPAL BOGOTÁ', 'Cra. 15 # 90-12', '6014568900', 'sede.bogota@clinica.com', 2);

INSERT INTO sedes (codigo_sede, nombre_sede, direccion, telefono, email, id_municipio)
VALUES ('SED002', 'SEDE NORTE MEDELLÍN', 'Cll. 30 # 65-40 El Poblado', '6044521100', 'sede.medellin@clinica.com', 1);

INSERT INTO sedes (codigo_sede, nombre_sede, direccion, telefono, email, id_municipio)
VALUES ('SED003', 'SEDE ATLÁNTICO', 'Cra. 53 # 68-120 El Prado', '6055623300', 'sede.barranquilla@clinica.com', 3);

INSERT INTO sedes (codigo_sede, nombre_sede, direccion, telefono, email, id_municipio)
VALUES ('SED004', 'SEDE CALI SUR', 'Av. 6 Norte # 30-11', '6024789900', 'sede.cali@clinica.com', 8);

INSERT INTO sedes (codigo_sede, nombre_sede, direccion, telefono, email, id_municipio)
VALUES ('SED005', 'SEDE BUCARAMANGA', 'Cll. 45 # 27-60 Cabecera', '6077891100', 'sede.bucaramanga@clinica.com', 7);

INSERT INTO sedes (codigo_sede, nombre_sede, direccion, telefono, email, id_municipio)
VALUES ('SED006', 'SEDE IBAGUÉ CENTRO', 'Cra. 4 # 13-30 Centro', '6082345600', 'sede.ibague@clinica.com', 9);


-- CONSULTORIOS (8 registros)

INSERT INTO consultorios (codigo_consultorio, nombre_consultorio, numero_piso, capacidad, id_sede)
VALUES ('CON001', 'CONSULTORIO MEDICINA GENERAL 101', 1, 1, 1);

INSERT INTO consultorios (codigo_consultorio, nombre_consultorio, numero_piso, capacidad, id_sede)
VALUES ('CON002', 'CONSULTORIO CARDIOLOGÍA 201', 2, 1, 1);

INSERT INTO consultorios (codigo_consultorio, nombre_consultorio, numero_piso, capacidad, id_sede)
VALUES ('CON003', 'CONSULTORIO PEDIATRÍA 102', 1, 2, 1);

INSERT INTO consultorios (codigo_consultorio, nombre_consultorio, numero_piso, capacidad, id_sede)
VALUES ('CON004', 'CONSULTORIO MEDICINA GENERAL 101', 1, 1, 2);

INSERT INTO consultorios (codigo_consultorio, nombre_consultorio, numero_piso, capacidad, id_sede)
VALUES ('CON005', 'CONSULTORIO GINECOLOGÍA 301', 3, 1, 2);

INSERT INTO consultorios (codigo_consultorio, nombre_consultorio, numero_piso, capacidad, id_sede)
VALUES ('CON006', 'CONSULTORIO ORTOPEDIA 202', 2, 1, 3);

INSERT INTO consultorios (codigo_consultorio, nombre_consultorio, numero_piso, capacidad, id_sede)
VALUES ('CON007', 'CONSULTORIO DERMATOLOGÍA 103', 1, 1, 4);

INSERT INTO consultorios (codigo_consultorio, nombre_consultorio, numero_piso, capacidad, id_sede)
VALUES ('CON008', 'SALA DE PROCEDIMIENTOS 401', 4, 3, 5);


-- ESPECIALIDADES

INSERT INTO especialidades (codigo_especialidad, nombre_especialidad)
VALUES ('MG', 'MEDICINA GENERAL');
INSERT INTO especialidades (codigo_especialidad, nombre_especialidad)
VALUES ('CARD', 'CARDIOLOGÍA');
INSERT INTO especialidades (codigo_especialidad, nombre_especialidad)
VALUES ('PED', 'PEDIATRÍA');
INSERT INTO especialidades (codigo_especialidad, nombre_especialidad)
VALUES ('GIN', 'GINECOLOGÍA');
INSERT INTO especialidades (codigo_especialidad, nombre_especialidad)
VALUES ('ORT', 'ORTOPEDIA');


-- PERSONAS / MEDICOS

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
) VALUES (
    'CC', '1001001001', 'LAURA', 'ANDREA',
    'GOMEZ', 'MORALES', DATE '1982-04-15', 'F',
    'laura.gomez@clinica.com', '3001234567',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '11001')
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
) VALUES (
    'CC', '1001001002', 'CARLOS', 'ENRIQUE',
    'RAMIREZ', 'TORRES', DATE '1978-09-22', 'M',
    'carlos.ramirez@clinica.com', '3002345678',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '05001')
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
) VALUES (
    'CC', '1001001003', 'MARIANA', NULL,
    'PEREZ', 'CASTILLO', DATE '1987-01-30', 'F',
    'mariana.perez@clinica.com', '3003456789',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '76001')
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
) VALUES (
    'CC', '1001001004', 'JUAN', 'PABLO',
    'MARTINEZ', 'ROJAS', DATE '1980-12-05', 'M',
    'juan.martinez@clinica.com', '3004567890',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '08001')
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
) VALUES (
    'CC', '1001001005', 'PAULA', 'SOFIA',
    'HERNANDEZ', 'LOPEZ', DATE '1985-06-18', 'F',
    'paula.hernandez@clinica.com', '3005678901',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '68001')
);

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
VALUES (
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001001'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'MG'),
    'RM-0001',
    85000
);

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
VALUES (
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001002'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'CARD'),
    'RM-0002',
    150000
);

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
VALUES (
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001003'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'PED'),
    'RM-0003',
    110000
);

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
VALUES (
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001004'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'GIN'),
    'RM-0004',
    130000
);

INSERT INTO medicos (id_persona, id_especialidad, numero_registro, tarifa_consulta)
VALUES (
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '1001001005'),
    (SELECT id_especialidad FROM especialidades WHERE codigo_especialidad = 'ORT'),
    'RM-0005',
    140000
);


-- PERSONAS / PACIENTES

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
) VALUES (
    'CC', '2002002001', 'ANA', 'MARIA',
    'RODRIGUEZ', 'SILVA', DATE '1990-03-12', 'F',
    'ana.rodriguez@email.com', '3101234567',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '11001')
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
) VALUES (
    'CC', '2002002002', 'MIGUEL', 'ANGEL',
    'SANCHEZ', 'DIAZ', DATE '1988-07-09', 'M',
    'miguel.sanchez@email.com', '3102345678',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '05001')
);

INSERT INTO personas (
    tipo_documento, numero_documento, primer_nombre, segundo_nombre,
    primer_apellido, segundo_apellido, fecha_nacimiento, sexo,
    email, telefono, id_municipio
) VALUES (
    'CC', '2002002003', 'CAMILA', NULL,
    'VARGAS', 'MEJIA', DATE '1995-11-25', 'F',
    'camila.vargas@email.com', '3103456789',
    (SELECT id_municipio FROM municipios WHERE codigo_dane = '76001')
);

INSERT INTO pacientes (id_persona, numero_historia, tipo_afiliacion, eps)
VALUES (
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '2002002001'),
    'HC-0001',
    'CONTRIBUTIVO',
    'SANITAS'
);

INSERT INTO pacientes (id_persona, numero_historia, tipo_afiliacion, eps)
VALUES (
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '2002002002'),
    'HC-0002',
    'SUBSIDIADO',
    'NUEVA EPS'
);

INSERT INTO pacientes (id_persona, numero_historia, tipo_afiliacion, eps)
VALUES (
    (SELECT id_persona FROM personas WHERE tipo_documento = 'CC' AND numero_documento = '2002002003'),
    'HC-0003',
    'PARTICULAR',
    NULL
);


-- CITAS MEDICAS

INSERT INTO citas_medicas (
    numero_cita, id_paciente, id_medico, id_consultorio,
    fecha_cita, hora_inicio, hora_fin, estado, motivo_consulta
) VALUES (
    'CITA-SEED-0001',
    (SELECT id_paciente FROM pacientes WHERE numero_historia = 'HC-0001'),
    (SELECT id_medico FROM medicos WHERE numero_registro = 'RM-0001'),
    (SELECT id_consultorio FROM consultorios WHERE codigo_consultorio = 'CON001' AND id_sede = 1),
    TRUNC(SYSDATE) + 1,
    '08:00',
    '08:30',
    'PROGRAMADA',
    'Consulta general'
);

INSERT INTO citas_medicas (
    numero_cita, id_paciente, id_medico, id_consultorio,
    fecha_cita, hora_inicio, hora_fin, estado, motivo_consulta
) VALUES (
    'CITA-SEED-0002',
    (SELECT id_paciente FROM pacientes WHERE numero_historia = 'HC-0002'),
    (SELECT id_medico FROM medicos WHERE numero_registro = 'RM-0002'),
    (SELECT id_consultorio FROM consultorios WHERE codigo_consultorio = 'CON002' AND id_sede = 1),
    TRUNC(SYSDATE) + 1,
    '09:00',
    '09:30',
    'CONFIRMADA',
    'Control cardiologia'
);

INSERT INTO citas_medicas (
    numero_cita, id_paciente, id_medico, id_consultorio,
    fecha_cita, hora_inicio, hora_fin, estado, motivo_consulta
) VALUES (
    'CITA-SEED-0003',
    (SELECT id_paciente FROM pacientes WHERE numero_historia = 'HC-0003'),
    (SELECT id_medico FROM medicos WHERE numero_registro = 'RM-0003'),
    (SELECT id_consultorio FROM consultorios WHERE codigo_consultorio = 'CON003' AND id_sede = 1),
    TRUNC(SYSDATE) + 2,
    '10:00',
    '10:30',
    'PROGRAMADA',
    'Valoracion pediatrica'
);

COMMIT;


-- VERIFICACIÓN DE DATOS INSERTADOS

SELECT 'DEPARTAMENTOS' tabla, COUNT(*) registros FROM departamentos
UNION ALL
SELECT 'MUNICIPIOS', COUNT(*) FROM municipios
UNION ALL
SELECT 'SEDES', COUNT(*) FROM sedes
UNION ALL
SELECT 'CONSULTORIOS', COUNT(*) FROM consultorios
UNION ALL
SELECT 'ESPECIALIDADES', COUNT(*) FROM especialidades
UNION ALL
SELECT 'PERSONAS', COUNT(*) FROM personas
UNION ALL
SELECT 'MEDICOS', COUNT(*) FROM medicos
UNION ALL
SELECT 'PACIENTES', COUNT(*) FROM pacientes
UNION ALL
SELECT 'CITAS_MEDICAS', COUNT(*) FROM citas_medicas;

-- FIN SCRIPT 04
