-- ============================================================
-- SCRIPT 05: TABLAS DE LA APLICACIÓN SPRING BOOT
-- Sistema de Gestión de Citas Médicas
-- Oracle XE 18c - Esquema: APP_CITAS
--
-- PROPÓSITO:
--   Crea las tablas que usa la capa de seguridad de Spring Boot
--   para autenticación JWT y control de acceso por roles/permisos.
--   Son COMPLEMENTARIAS a los roles de Oracle DB (Script 01 y 03).
--
-- Ejecutar como: app_citas
-- ============================================================


-- ─── TABLA 1: APP_PERMISOS ────────────────────────────────────────────
-- Permisos granulares: MODULO + ACCION (ej: CONSULTORIOS_CREATE)

CREATE TABLE app_permisos (
                              id_permiso      NUMBER          GENERATED ALWAYS AS IDENTITY
                                  (START WITH 1 INCREMENT BY 1)
                                  CONSTRAINT pk_permiso PRIMARY KEY,
                              nombre_permiso  VARCHAR2(100)   NOT NULL,
                              descripcion     VARCHAR2(255),
                              modulo          VARCHAR2(50),
                              accion          VARCHAR2(10)    CONSTRAINT ck_perm_accion
                                  CHECK (accion IN ('CREATE','READ','UPDATE','DELETE')),
                              activo          CHAR(1)         DEFAULT 'S'
                                  CONSTRAINT ck_perm_activo CHECK (activo IN ('S','N')),
                              CONSTRAINT uq_permiso_nombre UNIQUE (nombre_permiso)
)
    TABLESPACE tbs_citas_medicas;

COMMENT ON TABLE app_permisos IS 'Permisos de la app Spring Boot: MODULO_ACCION';
COMMENT ON COLUMN app_permisos.nombre_permiso IS 'Ej: CONSULTORIOS_CREATE, CITAS_READ';


-- ─── TABLA 2: APP_ROLES ───────────────────────────────────────────────
-- Roles de la aplicación (mapean con roles Oracle DB)

CREATE TABLE app_roles (
                           id_rol          NUMBER          GENERATED ALWAYS AS IDENTITY
                               (START WITH 1 INCREMENT BY 1)
                               CONSTRAINT pk_app_rol PRIMARY KEY,
                           nombre_rol      VARCHAR2(50)    NOT NULL,
                           descripcion     VARCHAR2(255),
                           activo          CHAR(1)         DEFAULT 'S'
                               CONSTRAINT ck_rol_activo CHECK (activo IN ('S','N')),
                           CONSTRAINT uq_rol_nombre UNIQUE (nombre_rol)
)
    TABLESPACE tbs_citas_medicas;

COMMENT ON TABLE app_roles IS 'Roles Spring Boot: MEDICO, PACIENTE, ADMINISTRATIVO, AUXILIAR_MEDICO';
COMMENT ON COLUMN app_roles.nombre_rol IS 'Sin prefijo ROLE_. Spring lo agrega automáticamente.';


-- ─── TABLA 3: APP_ROL_PERMISOS ────────────────────────────────────────
-- Relación M:N entre Roles y Permisos

CREATE TABLE app_rol_permisos (
                                  id_rol          NUMBER  NOT NULL,
                                  id_permiso      NUMBER  NOT NULL,
                                  CONSTRAINT pk_rol_permiso PRIMARY KEY (id_rol, id_permiso),
                                  CONSTRAINT fk_rp_rol     FOREIGN KEY (id_rol)     REFERENCES app_roles(id_rol),
                                  CONSTRAINT fk_rp_permiso FOREIGN KEY (id_permiso) REFERENCES app_permisos(id_permiso)
)
    TABLESPACE tbs_citas_medicas;


-- ─── TABLA 4: APP_USUARIOS ────────────────────────────────────────────
-- Usuarios de la aplicación para autenticación JWT

CREATE TABLE app_usuarios (
                              id_usuario      NUMBER          GENERATED ALWAYS AS IDENTITY
                                  (START WITH 1 INCREMENT BY 1)
                                  CONSTRAINT pk_app_usuario PRIMARY KEY,
                              username        VARCHAR2(50)    NOT NULL,
                              password_hash   VARCHAR2(255)   NOT NULL,
                              email           VARCHAR2(100),
                              activo          CHAR(1)         DEFAULT 'S'
                                  CONSTRAINT ck_usr_activo CHECK (activo IN ('S','N')),
                              fecha_creacion  DATE            DEFAULT SYSDATE,
                              ultimo_acceso   DATE,
                              id_persona      NUMBER,
                              CONSTRAINT uq_usr_username UNIQUE (username),
                              CONSTRAINT uq_usr_email    UNIQUE (email),
                              CONSTRAINT fk_usr_persona  FOREIGN KEY (id_persona)
                                  REFERENCES personas(id_persona)
)
    TABLESPACE tbs_citas_medicas;

COMMENT ON TABLE app_usuarios IS 'Usuarios Spring Boot para JWT. id_persona vincula con médico/paciente';


-- ─── TABLA 5: APP_USUARIO_ROLES ───────────────────────────────────────
-- Relación M:N entre Usuarios y Roles

CREATE TABLE app_usuario_roles (
                                   id_usuario      NUMBER  NOT NULL,
                                   id_rol          NUMBER  NOT NULL,
                                   CONSTRAINT pk_usuario_rol  PRIMARY KEY (id_usuario, id_rol),
                                   CONSTRAINT fk_ur_usuario   FOREIGN KEY (id_usuario) REFERENCES app_usuarios(id_usuario),
                                   CONSTRAINT fk_ur_rol       FOREIGN KEY (id_rol)     REFERENCES app_roles(id_rol)
)
    TABLESPACE tbs_citas_medicas;


-- ─── ÍNDICES ──────────────────────────────────────────────────────────

CREATE INDEX idx_usr_username ON app_usuarios(username);
CREATE INDEX idx_usr_persona  ON app_usuarios(id_persona);
CREATE INDEX idx_perm_modulo  ON app_permisos(modulo, accion);


-- ─── DATOS INICIALES: PERMISOS ────────────────────────────────────────
-- Genera permisos para cada módulo × cada acción CRUD

-- DEPARTAMENTOS
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('DEPARTAMENTOS_CREATE','Crear departamentos','DEPARTAMENTOS','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('DEPARTAMENTOS_READ',  'Consultar departamentos','DEPARTAMENTOS','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('DEPARTAMENTOS_UPDATE','Actualizar departamentos','DEPARTAMENTOS','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('DEPARTAMENTOS_DELETE','Eliminar departamentos','DEPARTAMENTOS','DELETE');

-- MUNICIPIOS
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('MUNICIPIOS_CREATE','Crear municipios','MUNICIPIOS','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('MUNICIPIOS_READ',  'Consultar municipios','MUNICIPIOS','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('MUNICIPIOS_UPDATE','Actualizar municipios','MUNICIPIOS','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('MUNICIPIOS_DELETE','Eliminar municipios','MUNICIPIOS','DELETE');

-- SEDES
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('SEDES_CREATE','Crear sedes','SEDES','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('SEDES_READ',  'Consultar sedes','SEDES','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('SEDES_UPDATE','Actualizar sedes','SEDES','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('SEDES_DELETE','Eliminar sedes','SEDES','DELETE');

-- CONSULTORIOS
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('CONSULTORIOS_CREATE','Crear consultorios','CONSULTORIOS','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('CONSULTORIOS_READ',  'Consultar consultorios','CONSULTORIOS','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('CONSULTORIOS_UPDATE','Actualizar consultorios','CONSULTORIOS','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('CONSULTORIOS_DELETE','Eliminar consultorios','CONSULTORIOS','DELETE');

-- PERSONAS
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('PERSONAS_CREATE','Registrar personas','PERSONAS','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('PERSONAS_READ',  'Consultar personas','PERSONAS','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('PERSONAS_UPDATE','Actualizar personas','PERSONAS','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('PERSONAS_DELETE','Eliminar personas','PERSONAS','DELETE');

-- MEDICOS
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('MEDICOS_CREATE','Registrar médicos','MEDICOS','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('MEDICOS_READ',  'Consultar médicos','MEDICOS','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('MEDICOS_UPDATE','Actualizar médicos','MEDICOS','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('MEDICOS_DELETE','Eliminar médicos','MEDICOS','DELETE');

-- PACIENTES
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('PACIENTES_CREATE','Registrar pacientes','PACIENTES','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('PACIENTES_READ',  'Consultar pacientes','PACIENTES','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('PACIENTES_UPDATE','Actualizar pacientes','PACIENTES','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('PACIENTES_DELETE','Eliminar pacientes','PACIENTES','DELETE');

-- CITAS
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('CITAS_CREATE','Agendar citas','CITAS','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('CITAS_READ',  'Consultar citas','CITAS','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('CITAS_UPDATE','Actualizar citas','CITAS','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('CITAS_DELETE','Cancelar citas','CITAS','DELETE');

-- USUARIOS / ROLES (solo admins)
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('USUARIOS_CREATE','Crear usuarios','USUARIOS','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('USUARIOS_READ',  'Consultar usuarios','USUARIOS','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('USUARIOS_UPDATE','Actualizar usuarios','USUARIOS','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('USUARIOS_DELETE','Inactivar usuarios','USUARIOS','DELETE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('ROLES_CREATE','Crear roles','ROLES','CREATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('ROLES_READ',  'Consultar roles','ROLES','READ');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('ROLES_UPDATE','Actualizar roles','ROLES','UPDATE');
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('ROLES_DELETE','Eliminar roles','ROLES','DELETE');

-- AUDITORIA
INSERT INTO app_permisos (nombre_permiso, descripcion, modulo, accion) VALUES ('AUDITORIA_READ','Ver auditoría','AUDITORIA','READ');


-- ─── DATOS INICIALES: ROLES ───────────────────────────────────────────

INSERT INTO app_roles (nombre_rol, descripcion)
VALUES ('MEDICO', 'Médico: lectura general + actualización de citas propias');

INSERT INTO app_roles (nombre_rol, descripcion)
VALUES ('PACIENTE', 'Paciente: solo consulta de sus propios datos y citas');

INSERT INTO app_roles (nombre_rol, descripcion)
VALUES ('ADMINISTRATIVO', 'Administrativo: acceso total a todas las funciones del sistema');

INSERT INTO app_roles (nombre_rol, descripcion)
VALUES ('AUXILIAR_MEDICO', 'Auxiliar: lectura general + registro de personas, pacientes y citas');

COMMIT;


-- ─── ASIGNACIÓN DE PERMISOS A ROLES ──────────────────────────────────

-- ROL MEDICO: solo lectura en casi todo + UPDATE en citas
INSERT INTO app_rol_permisos (id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM app_roles r, app_permisos p
WHERE r.nombre_rol = 'MEDICO'
  AND p.nombre_permiso IN (
                           'DEPARTAMENTOS_READ','MUNICIPIOS_READ','SEDES_READ','CONSULTORIOS_READ',
                           'ESPECIALIDADES_READ','PERSONAS_READ','MEDICOS_READ','PACIENTES_READ',
                           'CITAS_READ','CITAS_UPDATE'
    );

-- ROL PACIENTE: solo lectura básica
INSERT INTO app_rol_permisos (id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM app_roles r, app_permisos p
WHERE r.nombre_rol = 'PACIENTE'
  AND p.nombre_permiso IN (
                           'DEPARTAMENTOS_READ','MUNICIPIOS_READ','SEDES_READ','CONSULTORIOS_READ',
                           'PERSONAS_READ','PACIENTES_READ','MEDICOS_READ','ESPECIALIDADES_READ',
                           'CITAS_READ'
    );

-- ROL ADMINISTRATIVO: acceso total
INSERT INTO app_rol_permisos (id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM app_roles r, app_permisos p
WHERE r.nombre_rol = 'ADMINISTRATIVO';

-- ROL AUXILIAR_MEDICO: lectura + CREATE personas/pacientes/citas + UPDATE citas
INSERT INTO app_rol_permisos (id_rol, id_permiso)
SELECT r.id_rol, p.id_permiso
FROM app_roles r, app_permisos p
WHERE r.nombre_rol = 'AUXILIAR_MEDICO'
  AND p.nombre_permiso IN (
                           'DEPARTAMENTOS_READ','MUNICIPIOS_READ','SEDES_READ','CONSULTORIOS_READ',
                           'ESPECIALIDADES_READ','MEDICOS_READ',
                           'PERSONAS_READ','PERSONAS_CREATE',
                           'PACIENTES_READ','PACIENTES_CREATE',
                           'CITAS_READ','CITAS_CREATE','CITAS_UPDATE'
    );

COMMIT;


-- ─── USUARIO ADMINISTRADOR INICIAL ───────────────────────────────────
-- Contraseña: Admin2024# → hash BCrypt generado externamente
-- IMPORTANTE: reemplazar el hash por uno real antes de producción
-- Generar con: new BCryptPasswordEncoder().encode("Admin2024#")

INSERT INTO app_usuarios (username, password_hash, email, activo)
VALUES (
           'admin',
           '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQyCkSCO.f3tPLGZFjsEzPnLm',
           'admin@citasmedicas.com',
           'S'
       );

-- Asignar rol ADMINISTRATIVO al usuario admin
INSERT INTO app_usuario_roles (id_usuario, id_rol)
SELECT u.id_usuario, r.id_rol
FROM app_usuarios u, app_roles r
WHERE u.username = 'ADMIN' AND r.nombre_rol = 'ADMINISTRATIVO';

-- Usuario médico demo
INSERT INTO app_usuarios (username, password_hash, email, activo)
VALUES (
           'medico1',
           '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQyCkSCO.f3tPLGZFjsEzPnLm',
           'medico1@citasmedicas.com',
           'S'
       );

INSERT INTO app_usuario_roles (id_usuario, id_rol)
SELECT u.id_usuario, r.id_rol
FROM app_usuarios u, app_roles r
WHERE u.username = 'MEDICO1' AND r.nombre_rol = 'MEDICO';

-- Usuario auxiliar demo
INSERT INTO app_usuarios (username, password_hash, email, activo)
VALUES (
           'auxiliar1',
           '$2a$12$LQv3c1yqBWVHxkd0LHAkCOYz6TtxMQyCkSCO.f3tPLGZFjsEzPnLm',
           'auxiliar1@citasmedicas.com',
           'S'
       );

INSERT INTO app_usuario_roles (id_usuario, id_rol)
SELECT u.id_usuario, r.id_rol
FROM app_usuarios u, app_roles r
WHERE u.username = 'AUXILIAR1' AND r.nombre_rol = 'AUXILIAR_MEDICO';

COMMIT;


-- ─── VERIFICACIÓN FINAL ───────────────────────────────────────────────

SELECT 'APP_PERMISOS'     tabla, COUNT(*) total FROM app_permisos    UNION ALL
SELECT 'APP_ROLES'               , COUNT(*)      FROM app_roles       UNION ALL
SELECT 'APP_ROL_PERMISOS'        , COUNT(*)      FROM app_rol_permisos UNION ALL
SELECT 'APP_USUARIOS'            , COUNT(*)      FROM app_usuarios    UNION ALL
SELECT 'APP_USUARIO_ROLES'       , COUNT(*)      FROM app_usuario_roles;

-- Ver permisos asignados por rol
SELECT r.nombre_rol, p.modulo, p.accion, p.nombre_permiso
FROM app_roles r
         JOIN app_rol_permisos rp ON r.id_rol = rp.id_rol
         JOIN app_permisos p       ON p.id_permiso = rp.id_permiso
ORDER BY r.nombre_rol, p.modulo, p.accion;

-- FIN SCRIPT 05