# Sanos y Salvos — Documento ClickUp (levantamiento + backlog + flujo de trabajo)

Documento pensado para pegar en **ClickUp** como descripción de **Espacio / Lista / Documento** y para crear **tareas** con el mismo nivel de detalle. Incluye contexto de negocio, requisitos, microservicios, desglose **SY-\*** y prácticas de equipo (ramas, PR, estados).

---

## 1. Descripción del proyecto (pegar en ClickUp: descripción del Space o Folder)

### 1.1 Resumen ejecutivo

**Sanos y Salvos** es una plataforma tecnológica para la **búsqueda y recuperación de mascotas extraviadas** en Chile, con visión de expansión regional. El proyecto entrega una **arquitectura de microservicios cloud-native** que centraliza información, mejora la coordinación entre dueños, voluntarios e instituciones, y automatiza **coincidencias (matching)**, **notificaciones** y **analítica**.

### 1.2 Contexto y problemática (levantamiento)

La operación actual se ve afectada por:

| Problema                                  | Impacto                                                                                  |
| ----------------------------------------- | ---------------------------------------------------------------------------------------- |
| Publicaciones dispersas en redes sociales | Información fragmentada y difícil de rastrear                                            |
| Sin base de datos centralizada            | No hay registro estructurado (perdido/encontrado, ubicación, rasgos, dueño)              |
| Datos inconsistentes o desactualizados    | Rescate más lento (falta ubicación, fechas, fotos claras, contacto)                      |
| **Falta de coordinación**                 | Duplicación de esfuerzos, operativos descoordinados, omisión de zonas ya (no) revisadas  |
| **Falta de trazabilidad**                 | No hay un único punto de verdad ni notificación masiva con geolocalización precisa       |
| **Poca integración institucional**        | Veterinarias, refugios y municipalidades reciben animales pero no existe canal unificado |
| **Baja trazabilidad de encontrados**      | Sin placa/chip vinculado a datos, difícil reunir con el dueño                            |

### 1.3 Necesidades de software (objetivos del producto)

- **Plataforma centralizada** de registro (perdidos/encontrados, ubicación, rasgos, dueño/reportante).
- **Geolocalización** y mapas con zonas de mayor incidencia.
- **Motor de coincidencias** (algoritmo / IA) entre reportes perdido ↔ encontrado.
- **Integración con externos** (veterinarias, refugios, municipalidades, APIs).
- **Difusión automatizada** (alertas, notificaciones a usuarios cercanos).
- **Red colaborativa** (ciudadanos, voluntarios, organizaciones coordinadas).

### 1.4 Requisitos funcionales (resumen por dominio)

- **Reportes de mascotas:** CRUD reporte; estado; rasgos; fotos; contacto; geolocalización; fecha; búsqueda con filtros; lista y mapa; detalle.
- **Matching (IA):** detección automática de posibles coincidencias; visualización de sugerencias.
- **Geolocalización:** mapa interactivo.
- **Notificaciones:** por coincidencia; push a usuarios cercanos.
- **Gestión de usuarios:** registro, autenticación, perfiles.
- **Integración externa:** registro desde instituciones; APIs.
- **Difusión:** publicación automática en redes; alertas compartibles.
- **Coordinación:** rol voluntario; asignación de zonas de búsqueda.
- **Analítica:** históricos; zonas de alta incidencia.
- **Multimedia:** carga y almacenamiento de imágenes.
- **Estado:** marcar mascota como recuperada.

### 1.5 Requisitos no funcionales

| RNF                | Descripción                                                                        |
| ------------------ | ---------------------------------------------------------------------------------- |
| **Escalabilidad**  | Grandes volúmenes de reportes, imágenes y eventos; picos en campañas y alertas     |
| **Disponibilidad** | Servicios críticos (reportes, consulta) con tolerancia a fallos                    |
| **Seguridad**      | Autenticación/autorización (JWT), IAM, secretos, cifrado en tránsito               |
| **Mantenibilidad** | Microservicios por dominio, despliegues independientes                             |
| **Observabilidad** | Monitoreo en puntos clave (API Gateway, balanceador, cuellos de botella conocidos) |

### 1.6 Mapa de microservicios (visión técnica)

| Servicio                 | Responsabilidad                                                                                                            |
| ------------------------ | -------------------------------------------------------------------------------------------------------------------------- |
| **User Service**         | Usuarios: registro, login, JWT, roles, perfil, contacto                                                                    |
| **Pet Service**          | Mascotas: modelo y ciclo de vida; **alta habitual vía creación de reporte** (u opcional `petId`); características y estado |
| **Report Service**       | Reportes: CRUD, filtros, geo, avistamientos, asociación usuario/mascota                                                    |
| **Media Service**        | Archivos: subida, almacenamiento (p. ej. S3), URLs                                                                         |
| **Integration Service**  | Externos: APIs instituciones, transformación de datos, broadcast redes                                                     |
| **Matching Service**     | Coincidencias: comparación, IA/Rekognition, eventos                                                                        |
| **Notification Service** | Notificaciones: push, alertas zona, matching                                                                               |
| **Analytics Service**    | Métricas, zonas críticas, reportes estadísticos                                                                            |

### 1.7 Regla de dominio: creación de mascota al crear el reporte

**Decisión de producto:** en el flujo principal, **no es obligatorio dar de alta una mascota antes del reporte**. Al **crear un reporte** (`POST /reports` o equivalente), el sistema **crea la ficha de mascota (Pet)** en la misma operación lógica (transacción distribuida o orquestación entre **Report Service** y **Pet Service**), salvo que el cliente envíe un **`petId` ya existente** (dueño que reporta de nuevo la misma mascota).

| Escenario                 | Comportamiento                                                                                                                   |
| ------------------------- | -------------------------------------------------------------------------------------------------------------------------------- |
| Body **sin** `petId`      | Se crea **Pet** mínima o completa con los datos del reporte (rasgos del body) y el **Report** queda asociado a ese `petId` nuevo |
| Body **con** `petId`      | Se reutiliza la mascota; el reporte solo enlaza usuario + mascota existente                                                      |
| Institución externa / API | Mismo criterio: un reporte entrante puede materializar **Pet + Report** en un solo flujo                                         |

**Notas para implementación:** documentar políticas anti-duplicado (chip, mismo usuario + ventana temporal, etc.) en tareas SY-11 / SY-21. El **Matching** y **Media** siguen referenciando **Pet** y **Report** como hasta ahora.

---

## 2. Configuración recomendada en ClickUp

### 2.1 Estructura de Space / Folder / Listas

Sugerencia:

```text
Space: Sanos y Salvos
├── Folder: Producto — Backend
│   ├── List: 👤 User Service
│   ├── List: 🐾 Pet Service
│   ├── List: 📄 Report Service
│   ├── List: 📦 Media Service
│   ├── List: 🌐 Integration Service
│   ├── List: 🧠 Matching Service
│   ├── List: 🔔 Notification Service
│   ├── List: 📊 Analytics Service
│   └── List: ⚙️ Platform / DevOps / Seguridad / Eventos
├── Folder: Documentación
│   └── Doc: Este levantamiento (vinculado a GitHub / Drive si aplica)
```

### 2.2 Estados del flujo (Status)

Usar **los mismos en todas las listas** para reportes homogéneos:

1. **To Do** — Listo para tomar, dependencias claras
2. **In Progress** — Asignado y en desarrollo
3. **In Review** — PR abierto / code review / QA
4. **Done** — Merge a rama principal acordada (`develop` / `main`)

### 2.3 Campos personalizados sugeridos

| Campo             | Tipo                | Uso                          |
| ----------------- | ------------------- | ---------------------------- |
| **ID SY**         | Texto (ej. `SY-21`) | Trazabilidad única           |
| **Microservicio** | Dropdown            | User, Pet, Report, …         |
| **Rama Git**      | Texto               | `feature/SY-21-post-reports` |
| **Repositorio**   | URL                 | Link a GitHub                |
| **RF/RNF**        | Etiquetas           | Escalabilidad, seguridad, …  |

### 2.4 Cómo trabajar en equipo (convenciones)

- **Una tarea ClickUp** ↔ **un código SY-\*** ↔ **una rama** y **commits** que mencionen el ID.
- **Ejemplo:**
  - Tarea ClickUp: **SY-21 — POST /reports**
  - Rama: `feature/SY-21-post-reports`
  - Commit: `feat(report): implement POST /reports endpoint (SY-21)`
  - PR: `feature/SY-21-post-reports` → `develop`
- **Reparto ejemplo:**
  - Dev 1: User + Auth
  - Dev 2: Report Service
  - Dev 3: Matching + IA
  - Dev 4: Notifications + Analytics
  - DevOps / Platform: SY-80 en adelante (Docker, ECS, EventBridge, etc.)

---

## 3. Backlog detallado por servicio (copiar cada bloque como tarea o subtask)

> **Nota de alineación:** En implementación, roles pueden modelarse como `DUENO` / `VOLUNTARIO` / `ADMIN` (equivalente negocio: dueño, voluntario, admin). Ajustar textos de API en ClickUp si los paths reales difieren (`/api/usuarios/me` vs `/users/me`).

> **Nota Pet + Report:** la creación de **Pet** va acoplada al **alta del primer reporte** salvo `petId` opcional en el body. SY-21 coordina o delega en Pet Service (SY-10–SY-14); no se exige un “solo POST /pets” previo para el flujo ciudadano urgente.

---

### 👤 USER SERVICE

**Lista ClickUp:** `User Service`  
**Descripción de lista:** Autenticación, perfiles, roles y contacto; base para el resto de microservicios.

#### SY-1 — Crear entidad User

- **Tipo:** Feature / Backend
- **Descripción:** Modelo de dominio usuario: identificador, email único, credencial (hash), nombre, teléfono, rol, auditoría mínima si aplica.
- **Criterios de aceptación:**
  - Entidad persistida en BD acordada
  - Email único a nivel de base de datos
  - Contraseña nunca en claro

#### SY-2 — Registro de usuario

- **Endpoint referencia:** `POST` registro (equivalente a `/api/auth/register` o `/users` según contrato API Gateway)
- **Criterios:**
  - Validación de entrada (email, password fuerte, nombre)
  - Rechazo de registro directo como ADMIN si la política lo define
  - Respuesta sin exponer datos sensibles

#### SY-3 — Login

- **Endpoint referencia:** `POST` login (`/api/auth/login`)
- **Criterios:**
  - Credenciales inválidas → **401**
  - Credenciales válidas → token emitido (ver SY-4)

#### SY-4 — Generar JWT

- **Descripción:** Emisión de JWT con claims mınimos: `sub` (user id), `email`, `rol`, expiración.
- **Criterios:**
  - Secreto vía variable de entorno / Secrets Manager en prod
  - TTL configurable

#### SY-5 — Validar JWT (security filter / middleware)

- **Criterios:**
  - Rutas protegidas rechazan peticiones sin token válido
  - Authorities / roles disponibles para `@PreAuthorize` o equivalente

#### SY-6 — Obtener perfil

- **Endpoint referencia:** `GET /users/me` o `GET /api/usuarios/me`
- **Criterios:**
  - Solo el usuario autenticado accede a su perfil

#### SY-7 — Actualizar perfil

- **Endpoint referencia:** `PATCH` perfil (nombre, etc.)
- **Criterios:**
  - Validación de campos
  - No permite escalada de privilegio desde este endpoint

#### SY-8 — Roles (dueño / voluntario / admin)

- **Descripción:** Modelo de roles; endpoint administrativo para cambio de rol (solo ADMIN).
- **Criterios:**
  - Tres roles alineados al negocio
  - Política de quién puede promover a voluntario/admin documentada

---

### 🐾 PET SERVICE

**Lista:** `Pet Service`  
**Contexto:** La **alta habitual** de mascota ocurre **como parte del alta de reporte** (ver §1.7). Este servicio expone el modelo **Pet** y operaciones reutilizables; el endpoint “crear solo mascota” puede ser **opcional** (perfil, pre-registro) o interno.

| ID    | Tarea                                   | Detalle breve                                                                                                                                                                                                |
| ----- | --------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| SY-10 | Crear entidad Pet                       | ID, ref dueño/usuario, nombre, especie, raza, color, tamaño, estado (perdida/encontrada/recuperada), timestamps                                                                                              |
| SY-11 | API interna / uso orquestado: crear Pet | Invocable desde **Report Service** al crear reporte **sin** `petId`; o endpoint opcional `POST /pets` si el producto lo requiere. Siempre con `userId` del reportante o política para “encontrado” sin dueño |
| SY-12 | Actualizar características              | PATCH; permisos dueño/admin; coherente con datos mostrados en reportes                                                                                                                                       |
| SY-13 | Obtener mascota por ID                  | GET; autorización                                                                                                                                                                                            |
| SY-14 | Listar mascotas por usuario             | GET por `userId` del token o query segura (ej. “mis mascotas” derivadas de reportes o registros explícitos)                                                                                                  |

---

### 📄 REPORT SERVICE (crítico)

**Lista:** `Report Service`  
**Etiqueta sugerida:** `prioridad-alta`

| ID    | Tarea                     | Detalle breve                                                                                                                                                                                                                                                       |
| ----- | ------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| SY-20 | Crear entidad Report      | Tipo (perdido/encontrado/avistamiento), fechas, geo, `petId`, `userId`, refs multimedia, estado del reporte                                                                                                                                                         |
| SY-21 | POST /reports             | **Flujo principal:** si no viene `petId`, **crear Pet** (Pet Service) y luego **Report** enlazado; si viene `petId`, validar pertenencia/permisos y crear solo Report. Emitir evento si aplica (`pet_reported` / `pet_found`). Transacción única o saga documentada |
| SY-22 | GET /reports              | Listado paginado                                                                                                                                                                                                                                                    |
| SY-23 | Filtros                   | Ubicación, fecha, tipo, características (vía joins o índices con Pet)                                                                                                                                                                                               |
| SY-24 | Asociar mascota + usuario | Tras SY-21 queda `report.petId` + `report.userId`; reglas de integridad y caso “encontrado” sin dueño                                                                                                                                                               |
| SY-25 | Marcar como recuperado    | Actualizar estado en **Pet** y/o cerrar reportes activos según regla de negocio                                                                                                                                                                                     |
| SY-26 | Registrar avistamiento    | Sub-entidad o tabla ligada a Report/Pet; geo y timestamp                                                                                                                                                                                                            |

---

### 📦 MEDIA SERVICE

| ID    | Tarea                                  | Detalle breve                               |
| ----- | -------------------------------------- | ------------------------------------------- |
| SY-30 | Integración S3 (o almacén equivalente) | Bucket, políticas, OAC/CloudFront si aplica |
| SY-31 | Subir imagen                           | Presigned URL o multipart según diseño      |
| SY-32 | Generar URL pública / firmada          | TTL y permisos                              |
| SY-33 | Asociar imagen a reporte               | IDs y validación                            |

---

### 🌐 INTEGRATION SERVICE

| ID    | Tarea                                 | Detalle breve                        |
| ----- | ------------------------------------- | ------------------------------------ |
| SY-40 | Endpoint para externos                | API key / OAuth / mTLS según amenaza |
| SY-41 | Validar datos externos                | Schema + reglas                      |
| SY-42 | Transformar formato externo → interno | Mapeo documentado                    |
| SY-43 | Registrar reportes desde veterinarias | Flujo idempotente si es posible      |

---

### 🧠 MATCHING SERVICE (Lambda / asíncrono)

| ID    | Tarea                              | Detalle breve                                        |
| ----- | ---------------------------------- | ---------------------------------------------------- |
| SY-50 | Lógica de comparación              | Reglas + ponderación (raza, color, distancia, fecha) |
| SY-51 | Integrar Rekognition (u otro)      | Comparación visual                                   |
| SY-52 | Calcular score de coincidencia     | Umbral y explicación mínima para UI                  |
| SY-53 | Evento `match_found` / equivalente | Publicación a EventBridge o cola                     |

---

### 🔔 NOTIFICATION SERVICE (Lambda)

| ID    | Tarea                                   | Detalle breve             |
| ----- | --------------------------------------- | ------------------------- |
| SY-60 | Consumir EventBridge                    | Reglas IAM mínimas        |
| SY-61 | Notificar coincidencias                 | Plantilla + datos         |
| SY-62 | Notificar reportes cercanos             | Geo + radio               |
| SY-63 | Integrar proveedor push (FCM/APNs/etc.) | Claves en Secrets Manager |

---

### 📊 ANALYTICS SERVICE (Lambda)

| ID    | Tarea                              | Detalle breve           |
| ----- | ---------------------------------- | ----------------------- |
| SY-70 | Consumir eventos                   | Esquema unificado       |
| SY-71 | Guardar métricas DynamoDB (u otro) | Partición y TTL         |
| SY-72 | Zonas calientes / mapas de calor   | Agregaciones            |
| SY-73 | Reportes estadísticos              | Export o API de lectura |

---

### ⚙️ PLATFORM / DEVOPS / SEGURIDAD / EVENTOS

**Lista:** `Platform`

#### Infraestructura

| ID    | Tarea                               |
| ----- | ----------------------------------- |
| SY-80 | Dockerizar microservicios           |
| SY-81 | Repositorios / artefactos en ECR    |
| SY-82 | ECS Cluster                         |
| SY-83 | Fargate tasks y definiciones        |
| SY-84 | ALB + target groups + health checks |
| SY-85 | VPC, subnets públicas/privadas, NAT |

#### Seguridad

| ID    | Tarea                                           |
| ----- | ----------------------------------------------- |
| SY-90 | JWT global (API Gateway authorizer + servicios) |
| SY-91 | IAM roles por servicio / por función Lambda     |
| SY-92 | Secrets Manager / Parameter Store               |

#### Eventos

| ID     | Tarea                                                                 |
| ------ | --------------------------------------------------------------------- |
| SY-100 | EventBridge bus + reglas                                              |
| SY-101 | Contrato de eventos: `pet_reported`, `pet_found`, `match_found`, etc. |

---

## 4. Matriz rápida RF → microservicio (para descripción ClickUp o Wiki)

| RF (agrupado)                               | Servicios involucrados                              |
| ------------------------------------------- | --------------------------------------------------- |
| Reportes CRUD, filtros, mapa, avistamientos | Report (orquesta alta de **Pet**), Pet, User, Media |
| Matching e IA                               | Matching, Report, Pet, Media (fotos)                |
| Notificaciones                              | Notification, Report, User                          |
| Usuarios y perfiles                         | User                                                |
| Integración instituciones                   | Integration, Report, User                           |
| Difusión redes                              | Integration                                         |
| Analítica                                   | Analytics, Report                                   |
| Multimedia                                  | Media                                               |

---

## 5. Resultado esperado (criterio “profesional” del curso / entrega)

- Microservicios **separados por dominio** y **trazables** en ClickUp (ID SY + estado).
- **Integración ClickUp ↔ GitHub** (link de PR en cada tarea al cerrar _In Review_).
- **Flujo:** To Do → In Progress → In Review → Done con ramas y commits referenciados.
- Backend **organizado** (MVCS o equivalente por servicio), seguridad y eventos documentados.

---

## 6. Texto corto para “Descripción” del Space (una sola caja)

```text
Plataforma Sanos y Salvos: recuperación de mascotas extraviadas mediante microservicios en la nube. Regla clave: al crear un reporte se crea la mascota (Pet) salvo que se envíe petId existente. Centralizamos reportes, geolocalización, matching con IA, notificaciones y analítica; integramos veterinarias, refugios y municipalidades. Backlog SY-* por servicio + plataforma. Estados: To Do → In Progress → In Review → Done. Cada tarea enlaza rama Git y PR.
```

---

_Documento generado para uso en ClickUp y alineado al levantamiento funcional/técnico de Sanos y Salvos._
