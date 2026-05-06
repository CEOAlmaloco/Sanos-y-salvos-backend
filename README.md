# Sanos y Salvos - Microservicios Backend

Backend monorepo Java/Spring Boot con APIs REST, JWT, PostgreSQL y MinIO.

## Estado actual

Hoy están funcionales 5 microservicios:

| Servicio | Puerto local |
| --- | --- |
| `msvc-user` | `8081` |
| `msvc-report` | `8082` |
| `msvc-pet` | `8083` |
| `msvc-media` | `8084` |
| `msvc-integration` | `8085` |

Los módulos `msvc-matching`, `msvc-notification` y `msvc-analytics` están pensados para una siguiente etapa.

## Versión actual

| Componente | Versión |
| --- | --- |
| Proyecto | `1.0.0` |
| Spring Boot | `4.0.5` |
| Java | `21` |
| Spring Cloud BOM | `2025.1.0` |

## Requisitos

- JDK 21
- Maven
- Docker Desktop

## Estrategia de ramas

El proyecto usa un flujo de trabajo ambientado en multiples entornos:

### Ramas principales

- `main`: Producción, código estable
- `staging`: Pre-producción, validación final antes de producción
- `qa`: Testing, pruebas funcionales
- `develop`: Desarrollo

### Ramas de trabajo

Todas las ramas de trabajo se crean desde `develop`:

- `feature/nombre-feature`: nuevas funcionalidades
- `fix/nombre-bug`: corrección de errores
- `chore/nombre-tarea`: tareas técnicas

Ejemplos:

- `feature/pet-report`
- `fix/register-error`
- `chore/update-deps`

## Flujo de trabajo

### 1. Desarrollo

```bash
git checkout develop
git pull origin develop
git checkout -b feature/nueva-funcionalidad
```

### 2. Integración

- Pull Request: `feature/*` -> `develop`
- Revisión obligatoria

### 3. Testing (QA)

- Pull Request: `develop` -> `qa`
- Se realizan pruebas funcionales

### 4. Pre-producción

- Pull Request: `qa` -> `staging`
- Validación final: bugs críticos, configuración y rendimiento básico

### 5. Producción

- Pull Request: `staging` -> `main`
- Código estable listo para deploy

## Cómo levantar el proyecto

La forma recomendada es con Docker usando `docker-compose.yml`. Esto es necesario para levantar correctamente MinIO y el servicio de media.

```bash
docker compose up --build
```

Si prefieres correr los microservicios de forma manual, también funcionan, pero debes tener levantados PostgreSQL y MinIO, además de configurar las variables de entorno de cada servicio.

Importante: si ejecutas los microservicios manualmente desde IntelliJ sin Docker/MinIO, el `msvc-media` no funcionará correctamente.

## Variables de entorno

El proyecto usa un archivo `.env` en la raíz. Debe incluir, como mínimo, estas variables:

| Variable | Uso |
| --- | --- |
| `POSTGRES_USER` | Usuario de PostgreSQL |
| `POSTGRES_PASSWORD` | Contraseña de PostgreSQL |
| `POSTGRES_DB` | Nombre de la base de datos |
| `JWT_SECRET` | Secreto compartido para JWT |
| `JWT_EXPIRATION_MS` | Tiempo de expiración del token |
| `MINIO_ROOT_USER` | Usuario root de MinIO |
| `MINIO_ROOT_PASSWORD` | Contraseña root de MinIO |
| `MINIO_PRESIGN_ENDPOINT` | Endpoint público de MinIO para URLs firmadas |
| `SANOS_MEDIA_BUCKET` | Bucket usado por el servicio de media |

Valores por defecto usados por Docker:

| Servicio | URL interna |
| --- | --- |
| PostgreSQL | `postgres:5432` |
| MinIO | `http://minio:9000` |
| User | `http://msvc-user:8081` |
| Report | `http://msvc-report:8082` |
| Pet | `http://msvc-pet:8083` |
| Media | `http://msvc-media:8084` |

## Puertos expuestos en Docker

| Servicio | Puerto del host |
| --- | --- |
| PostgreSQL | `5433` |
| MinIO API | `9000` |
| MinIO Console | `9001` |
| `msvc-user` | `9081` |
| `msvc-report` | `9082` |
| `msvc-pet` | `9083` |
| `msvc-media` | `9084` |
| `msvc-integration` | `9085` |

## Base de datos

El compose ejecuta el script `docker/init-db.sql`, que crea los esquemas necesarios para `users_schema`, `pets_schema` e `integrations_schema`.

## Integration Service

El servicio de integración expone `POST /api/v1/integration/reports` y protege el acceso con una API key enviada en el header `X-Api-Key`.

Antes de probarlo, hay que crear manualmente un registro en la tabla `integrations_schema.institution_api_keys`.

### Paso a paso para cargar la API key

1. Levanta el proyecto con Docker.
2. Registra primero un usuario en el sistema y copia su `id`.

Endpoint:

```bash
POST /api/v1/auth/register
```

Body de ejemplo:

```json
{
	"name": "Camila",
	"lastName": "Rojas",
	"email": "camila.rojas@veterinarialasheras.cl",
  "password": "Password123",
  "confirmPassword": "Password123",
	"phone": "+56987654321"
}
```

3. Entra a PostgreSQL desde tu cliente SQL favorito, desde el contenedor o desde IntelliJ creando un datasource PostgreSQL.

	En IntelliJ:
	- Abre la ventana `Database`.
	- Agrega un datasource `PostgreSQL`.
	- Usa `localhost` como host.
	- Usa el puerto `5433`.
	- Usa el valor de `POSTGRES_DB`, `POSTGRES_USER` y `POSTGRES_PASSWORD` definidos en tu `.env`.
	- Prueba la conexión y luego ejecuta el `INSERT` de `institution_api_keys`.
4. Inserta un registro en `integrations_schema.institution_api_keys` con estos campos:
	- `id`
	- `institution_name`
	- `api_key`
	- `user_id`
	- `created_at`
	- en `user_id` debes pegar el `id` del usuario creado en el paso anterior
5. Guarda el valor de `api_key`, porque ese será el que luego debes enviar en el header `X-Api-Key`.

Ejemplo de SQL:

```sql
INSERT INTO integrations_schema.institution_api_keys (
    id, 
    institution_name, 
    api_key, 
    user_id, 
    created_at
)
VALUES (
           '11111111-1111-1111-1111-111111111111',
           'Veterinaria Las Heras',
           'vet-las-heras-key-2024',
           'ID_DE_USUARIO_CREADO',
           CURRENT_TIMESTAMP
       );
```

### Cómo crear un reporte desde Integration Service

Endpoint:

```bash
POST /api/v1/integration/reports
```

Headers:

```bash
X-Api-Key: vet-las-heras-key-2024
Content-Type: application/json
```

Body de ejemplo:

```json
{
	"externalReportId": "EXT-001",
	"reportType": "LOST",
	"petId": "3c1d7d6f-3f8a-4d7b-9d6e-2a8f8a3f4b21",
	"title": "Perro perdido en parque",
	"description": "Se perdió cerca del parque central.",
	"latitude": -33.4489,
	"longitude": -70.6693,
	"locationText": "Parque central, sector oriente",
	"eventDate": "2026-05-05T10:30:00"
}
```

La respuesta crea el reporte interno en el servicio de reportes y devuelve el `externalReportId`, el `internalReportId` y un mensaje de confirmación.

## Ejecución manual por servicio

Si no usas Docker, los servicios principales se levantan en estos puertos:

- `msvc-user`: `8081`
- `msvc-report`: `8082`
- `msvc-pet`: `8083`
- `msvc-media`: `8084`
- `msvc-integration`: `8085`

En ese caso debes configurar las mismas variables de entorno que usa `docker-compose.yml`, especialmente `JWT_SECRET`, `POSTGRES_*` y las credenciales de MinIO para `msvc-media`.

Si corres manualmente desde IntelliJ y no levantas MinIO, el servicio `msvc-media` quedará fuera de funcionamiento.

## Postman Global

En la carpeta `postman/` está la colección global `PostmanGlobal.json`, que reúne las peticiones de cada microservicio en un solo archivo.

La colección incluye los flujos de `msvc-user`, `msvc-pet`, `msvc-report`, `msvc-media` e `msvc-integration`, con variables listas para usar en local o en Docker.

Si el proyecto está levantado con Docker, usa los puertos del host `9081` a `9085`. Si lo ejecutas directo desde IntelliJ o desde el IDE, usa los puertos locales `8081` a `8085`.

## Protección de ramas

### `main`

- Pull Request obligatorio
- mínimo 1 aprobación
- sin push directo

### `staging`

- Pull Request obligatorio
- mínimo 1 aprobación

### `qa`

- Pull Request obligatorio
- mínimo 1 aprobación

### `develop`

- Pull Request obligatorio, recomendado

## Checklist de Pull Request

- Código probado
- No rompe funcionalidades existentes
- Sigue convención de commits
- PR revisado por al menos 1 integrante
- Rama actualizada con la base

## Buenas prácticas

- No trabajar directamente en ramas principales
- Mantener PR pequeños y claros
- Hacer commits descriptivos
- Eliminar ramas después del merge

## Convención de commits

Se usa Conventional Commits:

```bash
tipo: descripción
```

Ejemplos:

- `feat: add pet report creation`
- `fix: resolve authentication error`
- `docs: update git workflow`
