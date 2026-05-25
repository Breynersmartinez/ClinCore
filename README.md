# Medical Management

Sistema integral para la gestion de citas medicas. El proyecto esta compuesto por una aplicacion frontend en React, una API REST en Spring Boot y scripts Oracle para la preparacion de base de datos, datos iniciales, auditoria y reglas de soporte.

## Tabla de Contenido

- [Descripcion General](#descripcion-general)
- [Arquitectura](#arquitectura)
- [Componentes del Proyecto](#componentes-del-proyecto)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Estructura del Repositorio](#estructura-del-repositorio)
- [Base de Datos](#base-de-datos)
- [Backend](#backend)
- [Frontend](#frontend)
- [Ejecucion Local](#ejecucion-local)
- [Autenticacion y Autorizacion](#autenticacion-y-autorizacion)
- [Modulos Funcionales](#modulos-funcionales)
- [Documentacion Tecnica](#documentacion-tecnica)
- [Pruebas y Verificacion](#pruebas-y-verificacion)
- [Notas de Operacion](#notas-de-operacion)

## Descripcion General

Medical Management es una solucion para administrar procesos basicos de una institucion medica:

- Inicio de sesion de usuarios.
- Control de acceso por roles y permisos.
- Administracion de usuarios, roles y permisos.
- Gestion de pacientes.
- Gestion de medicos.
- Gestion de consultorios y sedes.
- Agendamiento, consulta, actualizacion de estado y cancelacion de citas medicas.
- Soporte de datos maestros geograficos mediante departamentos y municipios.
- Persistencia en Oracle bajo el esquema `APP_CITAS`.

El frontend consume la API REST del backend mediante JWT. La base de datos se administra mediante scripts SQL incluidos en el repositorio.

## Arquitectura

```text
Usuario
  |
  v
Frontend React / Vite
  |
  | HTTP + JWT
  v
Backend Spring Boot REST API
  |
  | JDBC / JPA
  v
Oracle Database - Esquema APP_CITAS
```

La aplicacion esta separada por responsabilidades:

- `frontend/clinic-app`: interfaz web.
- `backend/citas-medicas-api`: API REST y reglas de negocio.
- `db`: scripts SQL y archivos CSV para carga de datos.
- `docs`: documentacion tecnica y recursos de apoyo.

## Componentes del Proyecto

### Frontend

Aplicacion SPA desarrollada con React 18 y Vite. Implementa autenticacion, rutas protegidas, consumo de API mediante Axios, control visual por permisos y pantallas operativas para los modulos principales.

Ruta:

```text
frontend/clinic-app
```

### Backend

API REST desarrollada con Spring Boot 3. Expone endpoints para autenticacion, usuarios, roles, permisos, pacientes, medicos, citas, sedes, consultorios y departamentos. Utiliza Spring Security, JWT, Spring Data JPA y Oracle JDBC.

Ruta:

```text
backend/citas-medicas-api
```

### Base de Datos

Scripts SQL para crear usuarios, tablespace, tablas, privilegios, datos iniciales, triggers, procedures, functions y semillas de datos.

Ruta:

```text
db
```

## Tecnologias

| Capa | Tecnologia | Version / Detalle |
| --- | --- | --- |
| Frontend | React | 18.3.1 |
| Frontend | Vite | 5.4.3 |
| Frontend | React Router DOM | 6.26.2 |
| Frontend | Axios | 1.7.7 |
| Frontend | Tailwind CSS | 3.4.11 |
| Backend | Java | 17 |
| Backend | Spring Boot | 3.2.0 |
| Backend | Spring Security | Incluido en Spring Boot |
| Backend | Spring Data JPA | Incluido en Spring Boot |
| Backend | Oracle JDBC | 23.3.0.23.09 |
| Backend | JJWT | 0.12.3 |
| Backend | SpringDoc OpenAPI | 2.3.0 |
| Base de datos | Oracle Database | Configurado para `XEPDB1` |
| Build backend | Maven Wrapper | Incluido |
| Build frontend | npm | Requiere Node.js |

## Requisitos

- JDK 17 o superior.
- Node.js y npm.
- Oracle Database disponible localmente o en red.
- Acceso al PDB configurado como `XEPDB1`, o ajuste equivalente.
- Git, si se requiere clonar o versionar el repositorio.
- Navegador moderno para ejecutar la interfaz web.

## Estructura del Repositorio

```text
Medical_management
|-- backend
|   |-- citas-medicas-api       API REST Spring Boot
|   |-- http                    Colecciones o solicitudes HTTP de apoyo
|   |-- Dockerfile              Imagen del backend
|   `-- citas-medicas-api.zip   Archivo comprimido del backend
|-- db
|   |-- csv                     Archivos CSV para carga de datos
|   `-- sql
|       |-- sys                 Scripts para tareas SYS/administrativas
|       `-- app_citas           Scripts del esquema de aplicacion
|-- docs
|   |-- docs_backend            Recursos de documentacion backend
|   |-- docs_db                 Diccionario y documentacion de base de datos
|   |-- docs_frontend           Recursos de documentacion frontend
|   |-- ClinicCore.png          Recurso grafico del proyecto
|   `-- DOCUMENTACION_TECNICA.md
|-- frontend
|   |-- clinic-app              Aplicacion React/Vite
|   `-- README.md
|-- .gitignore
`-- README.md
```

## Base de Datos

El proyecto usa Oracle y el esquema `APP_CITAS`.

Archivos principales:

```text
db/sql/sys/01_crear_tablespace_usuarios.sql
db/sql/app_citas/02_crear_tablas.sql
db/sql/app_citas/03_asignar_privilegios.sql
db/sql/app_citas/04_datos_iniciales.sql
db/sql/app_citas/05_triggers_auditoria.sql
db/sql/app_citas/06_procedures.sql
db/sql/app_citas/07_functions.sql
db/sql/app_citas/08_seed_medicos.sql
db/sql/app_citas/09_seed_pacientes_citas.sql
db/sql/app_citas/script_05_tablas_app.sql
```

Archivos CSV disponibles:

```text
db/csv/departamentos.csv
db/csv/municipios.csv
db/csv/sedes.csv
db/csv/consultorios.csv
```

Orden recomendado de preparacion:

1. Ejecutar los scripts administrativos ubicados en `db/sql/sys`.
2. Ejecutar la creacion de tablas del esquema `APP_CITAS`.
3. Ejecutar privilegios, datos iniciales, triggers, procedures y functions.
4. Cargar los CSV requeridos.
5. Ejecutar los scripts seed de medicos, pacientes y citas si el entorno requiere datos de prueba.

La API tiene desactivada la generacion automatica del esquema:

```properties
spring.jpa.hibernate.ddl-auto=none
```

Por lo tanto, la base de datos debe existir antes de iniciar el backend.

## Backend

Ruta:

```text
backend/citas-medicas-api
```

### Caracteristicas

- API REST con contexto base `/api/v1`.
- Seguridad stateless mediante JWT.
- Autorizacion con permisos usando `@PreAuthorize`.
- Persistencia con Spring Data JPA.
- Conexion a Oracle mediante `ojdbc11`.
- Respuestas estandarizadas con `ApiResponse`.
- Manejo centralizado de excepciones.
- Swagger UI mediante SpringDoc OpenAPI.

### Configuracion Principal

Archivo:

```text
backend/citas-medicas-api/src/main/resources/application.properties
```

Valores relevantes:

```properties
server.port=8080
server.servlet.context-path=/api/v1

spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=app_citas
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.properties.hibernate.default_schema=APP_CITAS

app.cors.allowed-origins=http://localhost:3000,http://localhost:4200,http://localhost:5173,http://localhost:5174
```

### Comandos

Desde `backend/citas-medicas-api`:

```bash
./mvnw clean package
./mvnw spring-boot:run
./mvnw test
```

Ejecutar el artefacto generado:

```bash
java -jar target/citas-medicas-api-1.0.0.jar
```

URL base:

```text
http://localhost:8080/api/v1
```

Swagger UI:

```text
http://localhost:8080/api/v1/swagger-ui.html
```

OpenAPI JSON:

```text
http://localhost:8080/api/v1/api-docs
```

### Endpoints Principales

| Modulo | Endpoint base | Descripcion |
| --- | --- | --- |
| Autenticacion | `/auth` | Login, refresh token y logout |
| Usuarios | `/usuarios` | Gestion de usuarios del sistema |
| Roles | `/roles` | Gestion de roles y permisos asignados |
| Permisos | `/permisos` | Consulta de permisos |
| Pacientes | `/pacientes` | Gestion de pacientes |
| Medicos | `/medicos` | Gestion de medicos |
| Citas | `/citas` | Agendamiento y gestion de citas |
| Sedes | `/sedes` | Gestion de sedes |
| Consultorios | `/consultorios` | Gestion de consultorios |
| Departamentos | `/departamentos` | Gestion de departamentos |

## Frontend

Ruta:

```text
frontend/clinic-app
```

### Caracteristicas

- SPA con React 18.
- Enrutamiento con React Router DOM.
- Estilos con Tailwind CSS.
- Cliente HTTP centralizado con Axios.
- Interceptor para adjuntar `Authorization: Bearer <token>`.
- Renovacion automatica de access token usando refresh token.
- Rutas privadas y componentes de control de acceso.
- Persistencia de sesion en `localStorage`.

### Rutas Principales

| Ruta | Descripcion | Acceso |
| --- | --- | --- |
| `/login` | Inicio de sesion | Publico |
| `/dashboard` | Panel principal | Usuario autenticado |
| `/usuarios` | Administracion de usuarios | `USUARIOS_READ` |
| `/roles` | Administracion de roles | `ROLES_READ` |
| `/consultorios` | Administracion de consultorios | `CONSULTORIOS_READ` |
| `/medicos` | Administracion de medicos | `MEDICOS_READ` |
| `/pacientes` | Administracion de pacientes | `PACIENTES_READ` |
| `/citas` | Gestion de citas medicas | `CITAS_READ` |

### Configuracion de API

El cliente Axios apunta actualmente a:

```javascript
const BASE_URL = 'http://localhost:8080/api/v1'
```

Archivo:

```text
frontend/clinic-app/src/api/axiosConfig.js
```

Si el backend se despliega en otro host o puerto, actualice este valor o reemplacelo por una variable de entorno segun la estrategia de despliegue.

### Comandos

Desde `frontend/clinic-app`:

```bash
npm install
npm run dev
npm run build
npm run preview
```

URL local habitual de Vite:

```text
http://localhost:5173
```

## Ejecucion Local

### 1. Preparar Base de Datos

Ejecute los scripts SQL correspondientes en Oracle y asegure que el esquema `APP_CITAS` quede disponible con datos iniciales.

### 2. Iniciar Backend

```bash
cd backend/citas-medicas-api
./mvnw spring-boot:run
```

Verifique:

```text
http://localhost:8080/api/v1/swagger-ui.html
```

### 3. Iniciar Frontend

```bash
cd frontend/clinic-app
npm install
npm run dev
```

Abra:

```text
http://localhost:5173
```

### 4. Flujo Basico

1. Iniciar sesion desde `/login`.
2. El frontend guarda `accessToken`, `refreshToken` y datos de usuario en `localStorage`.
3. Axios adjunta el token en cada solicitud protegida.
4. Si el access token expira, el interceptor intenta renovarlo.
5. Las pantallas se habilitan segun roles y permisos recibidos desde el backend.

## Autenticacion y Autorizacion

El backend expone los siguientes endpoints publicos:

```text
POST /api/v1/auth/login
POST /api/v1/auth/refresh
POST /api/v1/auth/logout
```

Solicitud de login:

```json
{
  "username": "admin",
  "password": "password123"
}
```

Respuesta esperada:

```json
{
  "success": true,
  "message": "Login exitoso",
  "data": {
    "accessToken": "token.jwt",
    "refreshToken": "refresh.jwt",
    "tokenType": "Bearer",
    "username": "admin",
    "roles": ["ADMINISTRATIVO"],
    "permisos": ["USUARIOS_READ", "CITAS_READ"]
  },
  "timestamp": "2026-05-24T10:15:30"
}
```

Encabezado requerido para endpoints protegidos:

```http
Authorization: Bearer <accessToken>
```

Roles usados por el frontend:

```text
MEDICO
PACIENTE
ADMINISTRATIVO
AUXILIAR_MEDICO
```

Permisos principales:

```text
USUARIOS_READ, USUARIOS_CREATE, USUARIOS_UPDATE, USUARIOS_DELETE
ROLES_READ, ROLES_CREATE, ROLES_UPDATE, ROLES_DELETE
PACIENTES_READ, PACIENTES_CREATE, PACIENTES_UPDATE, PACIENTES_DELETE
MEDICOS_READ, MEDICOS_CREATE, MEDICOS_UPDATE, MEDICOS_DELETE
CITAS_READ, CITAS_CREATE, CITAS_UPDATE, CITAS_DELETE
SEDES_READ, SEDES_CREATE, SEDES_UPDATE, SEDES_DELETE
CONSULTORIOS_READ, CONSULTORIOS_CREATE, CONSULTORIOS_UPDATE, CONSULTORIOS_DELETE
DEPARTAMENTOS_READ, DEPARTAMENTOS_CREATE, DEPARTAMENTOS_UPDATE, DEPARTAMENTOS_DELETE
```

## Modulos Funcionales

### Usuarios

Permite listar, crear, activar, inactivar, cambiar contrasena y asignar roles a usuarios del sistema.

### Roles y Permisos

Permite crear roles, consultar roles, actualizar permisos asociados e inactivar roles.

### Pacientes

Permite crear, consultar, actualizar e inactivar pacientes, asociandolos a personas existentes en la base de datos.

### Medicos

Permite crear, consultar, actualizar e inactivar medicos, asociandolos a una persona y una especialidad.

### Citas Medicas

Permite listar citas, consultar por paciente o medico, agendar nuevas citas, actualizar datos, cambiar estado y cancelar.

Estados soportados:

```text
PROGRAMADA
CONFIRMADA
ATENDIDA
CANCELADA
NO_ASISTIO
```

### Sedes y Consultorios

Permite administrar puntos de atencion y consultorios asociados a sedes.

### Departamentos y Municipios

Permite gestionar datos geograficos de soporte. Los municipios se encuentran modelados y disponibles como recurso de base de datos.

## Documentacion Tecnica

Recursos incluidos:

```text
docs/DOCUMENTACION_TECNICA.md
docs/docs_db/data_dictionary.md
docs/docs_backend/HELP.md
docs/docs_frontend/sistema_gestion_citas_medicas.html
backend/citas-medicas-api/README.md
```

El README especifico del backend contiene informacion detallada de endpoints, permisos, configuracion y estructura interna de la API.

## Pruebas y Verificacion

Backend:

```bash
cd backend/citas-medicas-api
./mvnw test
```

Frontend:

```bash
cd frontend/clinic-app
npm run build
```

Verificacion manual recomendada:

1. Confirmar que Oracle este disponible.
2. Iniciar backend y revisar Swagger UI.
3. Iniciar frontend.
4. Iniciar sesion con un usuario valido.
5. Validar que las pantallas visibles correspondan con los permisos del usuario.
6. Probar el flujo de creacion y consulta de citas.

## Notas de Operacion

- No publicar credenciales, secretos JWT ni cadenas de conexion reales en repositorios publicos.
- Externalizar configuracion sensible mediante variables de entorno, perfiles de Spring o gestor de secretos.
- Revisar `app.cors.allowed-origins` antes de desplegar en ambientes diferentes a desarrollo.
- Cambiar el secreto JWT antes de usar el sistema en produccion.
- Restringir Swagger UI en produccion si la politica de seguridad lo requiere.
- Mantener sincronizados los permisos configurados en base de datos, backend y frontend.
- La eliminacion funcional se maneja mediante inactivacion de registros cuando el modulo lo soporta.

