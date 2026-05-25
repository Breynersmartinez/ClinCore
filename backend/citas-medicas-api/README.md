# Citas Medicas API

API REST para la gestion de citas medicas, construida con Spring Boot 3, Spring Security, JWT, Spring Data JPA y Oracle Database. El sistema permite administrar usuarios, roles, permisos, pacientes, medicos, sedes, consultorios, departamentos y citas medicas mediante endpoints protegidos por autenticacion y autorizacion basada en permisos.

## Tabla de Contenido

- [Descripcion General](#descripcion-general)
- [Caracteristicas](#caracteristicas)
- [Tecnologias](#tecnologias)
- [Requisitos](#requisitos)
- [Configuracion](#configuracion)
- [Ejecucion](#ejecucion)
- [Documentacion OpenAPI](#documentacion-openapi)
- [Autenticacion y Seguridad](#autenticacion-y-seguridad)
- [Formato de Respuesta](#formato-de-respuesta)
- [Endpoints Principales](#endpoints-principales)
- [Estructura del Proyecto](#estructura-del-proyecto)
- [Modelo de Datos](#modelo-de-datos)
- [Pruebas](#pruebas)
- [Notas de Operacion](#notas-de-operacion)

## Descripcion General

Este proyecto implementa el backend de un sistema de gestion de citas medicas. La API expone servicios para:

- Autenticar usuarios mediante JWT.
- Consultar y administrar usuarios del sistema.
- Gestionar roles y permisos de aplicacion.
- Registrar y actualizar pacientes.
- Registrar y actualizar medicos.
- Administrar sedes y consultorios.
- Consultar y administrar departamentos.
- Agendar, modificar, consultar y cancelar citas medicas.

La aplicacion utiliza el contexto base `/api/v1` y se ejecuta por defecto en el puerto `8080`.

## Caracteristicas

- API REST con Spring Web.
- Persistencia con Spring Data JPA e Hibernate.
- Conexion a Oracle Database mediante `ojdbc11`.
- Seguridad stateless con Spring Security y JWT.
- Control de acceso por permisos mediante `@PreAuthorize`.
- Validacion de solicitudes con Jakarta Bean Validation.
- Manejo centralizado de excepciones.
- Respuestas estandarizadas mediante `ApiResponse`.
- Documentacion interactiva con SpringDoc OpenAPI y Swagger UI.
- Soporte CORS configurable para clientes frontend.

## Tecnologias

| Tecnologia | Version |
| --- | --- |
| Java | 17 |
| Spring Boot | 3.2.0 |
| Maven | Wrapper incluido |
| Oracle JDBC | 23.3.0.23.09 |
| JJWT | 0.12.3 |
| Lombok | 1.18.30 |
| MapStruct | 1.5.5.Final |
| SpringDoc OpenAPI | 2.3.0 |

## Requisitos

- JDK 17 o superior.
- Oracle Database accesible desde la aplicacion.
- Maven, o el wrapper incluido en el proyecto.
- Esquema de base de datos configurado para la aplicacion.
- Usuario de base de datos con permisos sobre el esquema `APP_CITAS`.

## Configuracion

La configuracion principal se encuentra en:

```text
src/main/resources/application.properties
```

Configuracion actual relevante:

```properties
server.port=8080
server.servlet.context-path=/api/v1

spring.datasource.url=jdbc:oracle:thin:@localhost:1521/XEPDB1
spring.datasource.username=app_citas
spring.datasource.driver-class-name=oracle.jdbc.OracleDriver

spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.default_schema=APP_CITAS

app.jwt.expiration-ms=86400000
app.jwt.refresh-expiration-ms=604800000
app.cors.allowed-origins=http://localhost:3000,http://localhost:4200,http://localhost:5173,http://localhost:5174
```

Antes de ejecutar la aplicacion, verifique:

- Que Oracle este iniciado y escuchando en `localhost:1521/XEPDB1`.
- Que exista el usuario de base de datos configurado.
- Que las tablas requeridas existan en el esquema `APP_CITAS`.
- Que el secreto JWT sea reemplazado por un valor seguro para entornos productivos.
- Que las credenciales sensibles no se publiquen en repositorios publicos.

## Ejecucion

Compilar el proyecto:

```bash
./mvnw clean package
```

Ejecutar la aplicacion:

```bash
./mvnw spring-boot:run
```

Tambien puede ejecutar el artefacto generado:

```bash
java -jar target/citas-medicas-api-1.0.0.jar
```

URL base local:

```text
http://localhost:8080/api/v1
```

## Documentacion OpenAPI

La API incluye documentacion interactiva con Swagger UI.

```text
http://localhost:8080/api/v1/swagger-ui.html
```

El documento OpenAPI se expone en:

```text
http://localhost:8080/api/v1/api-docs
```

## Autenticacion y Seguridad

La autenticacion se realiza mediante JWT. Los endpoints publicos son:

- `POST /auth/login`
- `POST /auth/refresh`
- `POST /auth/logout`
- `/swagger-ui/**`
- `/api-docs/**`
- `/swagger-ui.html`

Los demas endpoints requieren un token valido en el encabezado:

```http
Authorization: Bearer <accessToken>
```

### Inicio de Sesion

Solicitud:

```http
POST /api/v1/auth/login
Content-Type: application/json
```

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
    "roles": ["ADMIN"],
    "permisos": ["USUARIOS_READ", "CITAS_CREATE"]
  },
  "timestamp": "2026-05-24T10:15:30"
}
```

### Renovacion de Token

```http
POST /api/v1/auth/refresh?refreshToken=<refreshToken>
```

### Permisos

La autorizacion se define por permisos especificos. Ejemplos:

- `USUARIOS_READ`, `USUARIOS_CREATE`, `USUARIOS_UPDATE`, `USUARIOS_DELETE`
- `ROLES_READ`, `ROLES_CREATE`, `ROLES_UPDATE`, `ROLES_DELETE`
- `PACIENTES_READ`, `PACIENTES_CREATE`, `PACIENTES_UPDATE`, `PACIENTES_DELETE`
- `MEDICOS_READ`, `MEDICOS_CREATE`, `MEDICOS_UPDATE`, `MEDICOS_DELETE`
- `CITAS_READ`, `CITAS_CREATE`, `CITAS_UPDATE`, `CITAS_DELETE`
- `SEDES_READ`, `SEDES_CREATE`, `SEDES_UPDATE`, `SEDES_DELETE`
- `CONSULTORIOS_READ`, `CONSULTORIOS_CREATE`, `CONSULTORIOS_UPDATE`, `CONSULTORIOS_DELETE`
- `DEPARTAMENTOS_READ`, `DEPARTAMENTOS_CREATE`, `DEPARTAMENTOS_UPDATE`, `DEPARTAMENTOS_DELETE`

## Formato de Respuesta

Todas las respuestas exitosas o de error usan una estructura comun:

```json
{
  "success": true,
  "message": "Operacion realizada correctamente",
  "data": {},
  "timestamp": "2026-05-24T10:15:30",
  "totalElementos": 1
}
```

En caso de error:

```json
{
  "success": false,
  "message": "No tiene permisos para realizar esta accion",
  "timestamp": "2026-05-24T10:15:30"
}
```

Codigos de error manejados:

- `400 Bad Request`: errores de validacion o reglas de negocio.
- `401 Unauthorized`: credenciales invalidas o token ausente/invalido.
- `403 Forbidden`: usuario autenticado sin permisos suficientes.
- `404 Not Found`: recurso no encontrado.
- `500 Internal Server Error`: error inesperado.

## Endpoints Principales

Todos los endpoints se encuentran bajo el prefijo:

```text
/api/v1
```

### Autenticacion

| Metodo | Endpoint | Descripcion | Acceso |
| --- | --- | --- | --- |
| POST | `/auth/login` | Inicia sesion y retorna tokens JWT | Publico |
| POST | `/auth/refresh` | Renueva el access token | Publico |
| POST | `/auth/logout` | Cierra sesion en el cliente | Publico |

### Usuarios

| Metodo | Endpoint | Descripcion | Permiso |
| --- | --- | --- | --- |
| GET | `/usuarios` | Lista usuarios | `USUARIOS_READ` |
| GET | `/usuarios/{id}` | Busca usuario por ID | `USUARIOS_READ` |
| POST | `/usuarios` | Crea usuario con roles | `USUARIOS_CREATE` |
| PUT | `/usuarios/{id}/roles` | Reemplaza roles del usuario | `USUARIOS_UPDATE` |
| PATCH | `/usuarios/{id}/password` | Cambia la contrasena | `USUARIOS_UPDATE` |
| PATCH | `/usuarios/{id}/activar` | Activa un usuario | `USUARIOS_UPDATE` |
| DELETE | `/usuarios/{id}` | Inactiva un usuario | `USUARIOS_DELETE` |

### Roles y Permisos

| Metodo | Endpoint | Descripcion | Permiso |
| --- | --- | --- | --- |
| GET | `/roles` | Lista roles con permisos | `ROLES_READ` |
| GET | `/roles/{id}` | Busca rol por ID | `ROLES_READ` |
| POST | `/roles` | Crea rol con permisos | `ROLES_CREATE` |
| PUT | `/roles/{id}` | Actualiza rol | `ROLES_UPDATE` |
| PUT | `/roles/{id}/permisos` | Reemplaza permisos del rol | `ROLES_UPDATE` |
| DELETE | `/roles/{id}` | Inactiva rol | `ROLES_DELETE` |
| GET | `/permisos` | Lista permisos, opcionalmente por modulo | `ROLES_READ` o `USUARIOS_READ` |

### Pacientes

| Metodo | Endpoint | Descripcion | Permiso |
| --- | --- | --- | --- |
| GET | `/pacientes` | Lista pacientes | `PACIENTES_READ` |
| GET | `/pacientes/activos` | Lista pacientes activos | `PACIENTES_READ` |
| GET | `/pacientes/{id}` | Busca paciente por ID | `PACIENTES_READ` |
| GET | `/pacientes/persona/{idPersona}` | Busca paciente por persona | `PACIENTES_READ` |
| POST | `/pacientes` | Crea paciente | `PACIENTES_CREATE` |
| PUT | `/pacientes/{id}` | Actualiza paciente | `PACIENTES_UPDATE` |
| DELETE | `/pacientes/{id}` | Inactiva paciente | `PACIENTES_DELETE` |

### Medicos

| Metodo | Endpoint | Descripcion | Permiso |
| --- | --- | --- | --- |
| GET | `/medicos` | Lista medicos | `MEDICOS_READ` |
| GET | `/medicos/activos` | Lista medicos activos | `MEDICOS_READ` |
| GET | `/medicos/{id}` | Busca medico por ID | `MEDICOS_READ` |
| GET | `/medicos/persona/{idPersona}` | Busca medico por persona | `MEDICOS_READ` |
| GET | `/medicos/especialidad/{idEspecialidad}` | Lista medicos por especialidad | `MEDICOS_READ` |
| GET | `/medicos/especialidad/{idEspecialidad}/activos` | Lista medicos activos por especialidad | `MEDICOS_READ` |
| POST | `/medicos` | Crea medico | `MEDICOS_CREATE` |
| PUT | `/medicos/{id}` | Actualiza medico | `MEDICOS_UPDATE` |
| DELETE | `/medicos/{id}` | Inactiva medico | `MEDICOS_DELETE` |

### Citas Medicas

| Metodo | Endpoint | Descripcion | Permiso |
| --- | --- | --- | --- |
| GET | `/citas` | Lista citas | `CITAS_READ` |
| GET | `/citas/{id}` | Busca cita por ID | `CITAS_READ` |
| GET | `/citas/paciente/{idPaciente}` | Lista citas de un paciente | `CITAS_READ` |
| GET | `/citas/medico/{idMedico}` | Lista citas de un medico | `CITAS_READ` |
| POST | `/citas` | Agenda una cita | `CITAS_CREATE` |
| PUT | `/citas/{id}` | Modifica una cita | `CITAS_UPDATE` |
| PATCH | `/citas/{id}/estado` | Actualiza estado de cita | `CITAS_UPDATE` |
| DELETE | `/citas/{id}` | Cancela una cita | `CITAS_DELETE` |

Estados de cita soportados:

- `PROGRAMADA`
- `CONFIRMADA`
- `ATENDIDA`
- `CANCELADA`
- `NO_ASISTIO`

Ejemplo de solicitud para agendar cita:

```json
{
  "idPaciente": 1,
  "idMedico": 2,
  "idConsultorio": 3,
  "fechaCita": "2026-05-25",
  "horaInicio": "08:00",
  "horaFin": "08:30",
  "motivoConsulta": "Consulta general",
  "observaciones": "Primera consulta"
}
```

Actualizar estado:

```http
PATCH /api/v1/citas/1/estado?estado=CONFIRMADA&observaciones=Confirmada por telefono
```

Cancelar cita:

```http
DELETE /api/v1/citas/1?motivo=Solicitud del paciente
```

### Sedes

| Metodo | Endpoint | Descripcion | Permiso |
| --- | --- | --- | --- |
| GET | `/sedes` | Lista sedes activas | `SEDES_READ` |
| GET | `/sedes/{id}` | Busca sede por ID | `SEDES_READ` |
| POST | `/sedes` | Crea sede | `SEDES_CREATE` |
| PUT | `/sedes/{id}` | Actualiza sede | `SEDES_UPDATE` |
| DELETE | `/sedes/{id}` | Inactiva sede | `SEDES_DELETE` |

### Consultorios

| Metodo | Endpoint | Descripcion | Permiso |
| --- | --- | --- | --- |
| GET | `/consultorios` | Lista consultorios | `CONSULTORIOS_READ` |
| GET | `/consultorios/{id}` | Busca consultorio por ID | `CONSULTORIOS_READ` |
| GET | `/consultorios/sede/{idSede}` | Lista consultorios por sede | `CONSULTORIOS_READ` |
| GET | `/consultorios/sede/{idSede}/activos` | Lista consultorios activos por sede | `CONSULTORIOS_READ` |
| POST | `/consultorios` | Crea consultorio | `CONSULTORIOS_CREATE` |
| PUT | `/consultorios/{id}` | Actualiza consultorio | `CONSULTORIOS_UPDATE` |
| DELETE | `/consultorios/{id}` | Inactiva consultorio | `CONSULTORIOS_DELETE` |

### Departamentos

| Metodo | Endpoint | Descripcion | Permiso |
| --- | --- | --- | --- |
| GET | `/departamentos` | Lista departamentos activos | `DEPARTAMENTOS_READ` |
| GET | `/departamentos/{id}` | Busca departamento por ID | `DEPARTAMENTOS_READ` |
| POST | `/departamentos` | Crea departamento | `DEPARTAMENTOS_CREATE` |
| PUT | `/departamentos/{id}` | Actualiza departamento | `DEPARTAMENTOS_UPDATE` |
| DELETE | `/departamentos/{id}` | Inactiva departamento | `DEPARTAMENTOS_DELETE` |

## Estructura del Proyecto

```text
src/main/java/com/example/citas_medicas_api
|-- auth                 Autenticacion y emision de tokens
|-- baseEntity           Campos comunes de auditoria y estado
|-- config               Configuracion general y DTO de respuesta
|-- department           Gestion de departamentos
|-- doctor               Gestion de medicos
|-- exception            Manejo centralizado de excepciones
|-- headquarters         Gestion de sedes
|-- jwt                  Filtro, utilidades y carga de usuarios JWT
|-- medicalAppointment   Gestion de citas medicas
|-- municipality         Modelo y repositorio de municipios
|-- office               Gestion de consultorios
|-- patient              Gestion de pacientes
|-- permission           Consulta de permisos
|-- person               Modelo y repositorio de personas
|-- role                 Gestion de roles
|-- security             Configuracion de Spring Security
|-- specialty            Modelo y repositorio de especialidades
`-- user                 Gestion de usuarios
```

Cada modulo funcional sigue una estructura similar:

```text
controller/   Controladores REST
DTO/          Objetos de entrada y salida
model/        Entidades JPA
repository/   Repositorios Spring Data
service/      Reglas de negocio
```

## Modelo de Datos

El proyecto trabaja sobre el esquema Oracle `APP_CITAS`. Algunas entidades principales son:

| Entidad | Tabla |
| --- | --- |
| Usuario | `APP_USUARIOS` |
| Rol | `APP_ROLES` |
| Permiso | `APP_PERMISOS` |
| Persona | `PERSONAS` |
| Paciente | `PACIENTES` |
| Medico | `MEDICOS` |
| Especialidad | `ESPECIALIDADES` |
| Departamento | `DEPARTAMENTOS` |
| Municipio | `MUNICIPIOS` |
| Sede | `SEDES` |
| Consultorio | `CONSULTORIOS` |
| Cita medica | `CITAS_MEDICAS` |

La generacion automatica de esquema esta desactivada:

```properties
spring.jpa.hibernate.ddl-auto=none
```

Por lo tanto, la base de datos debe existir previamente con las tablas, relaciones, secuencias o identidades, restricciones y datos iniciales requeridos.

## Validaciones Principales

- Los campos requeridos se validan con `@NotBlank` y `@NotNull`.
- Los correos se validan con `@Email`.
- Las fechas de cita no pueden estar en el pasado.
- Las horas de cita deben usar formato `HH:mm`.
- La capacidad de consultorio debe estar entre `1` y `50`.
- La tarifa de consulta del medico debe ser mayor a cero cuando se informe.
- Los endpoints de citas validan reglas de negocio como solapamiento de horarios.

## Pruebas

Ejecutar las pruebas:

```bash
./mvnw test
```

La prueba incluida actualmente valida la carga del contexto de Spring Boot:

```text
src/test/java/com/example/citas_medicas_api/CitasMedicasApiApplicationTests.java
```

Para ejecutar pruebas de integracion, asegure que la base de datos Oracle este disponible con la configuracion definida en `application.properties`, o configure un perfil de pruebas independiente.

## Notas de Operacion

- El sistema usa JWT stateless; el cierre de sesion debe eliminar los tokens en el cliente.
- El control de permisos se aplica a nivel de metodo con `@PreAuthorize`.
- CORS esta limitado a los origenes configurados en `app.cors.allowed-origins`.
- Las eliminaciones funcionales inactivan registros mediante el campo `activo`, en lugar de realizar eliminacion fisica.
- Las credenciales, secretos y configuraciones sensibles deben externalizarse mediante variables de entorno, perfiles de Spring o un gestor de secretos en ambientes productivos.
- Swagger UI debe restringirse o deshabilitarse en produccion si la politica de seguridad del despliegue lo requiere.
