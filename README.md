# Sanos y Salvos — Backend (Java / Spring Boot)

APIs REST, JWT, Docker, CI/CD y despliegue cloud-native (ECS, Lambda, SQS, API Gateway).

## Estructura del monorepo

```text
Sanos-y-salvos-backend/
├── pom.xml                      ← POM padre (Spring Boot 4.0.5, Java 21)
├── msvc-user/                   ← usuarios y autenticación (8081)
├── msvc-pet/                    ← mascotas (8083)
├── msvc-report/                 ← reportes y EventBridge (8082)
├── msvc-media/                  ← fotos MinIO/S3 (8084)
├── msvc-integration/            ← API externa con X-Api-Key (8085)
├── msvc-matching/               ← Lambda: matching + Rekognition
├── msvc-notification/           ← Lambda: alertas SNS
├── msvc-analytics/              ← Lambda: consumidor SQS + API stats
├── docker/                      ← Dockerfiles por microservicio
├── docker-compose.yml           ← entorno local
├── docker-compose.swarm.yml     ← stack Docker Swarm
├── .github/workflows/ci-cd.yml  ← pipeline CI/CD
└── postman/                     ← colecciones de prueba
```

| Componente | Versión |
| ------------ | ------- |
| Spring Boot | 4.0.5 |
| Java | 21 |
| Spring Cloud BOM | 2025.1.0 |

Puertos internos: user **8081**, report **8082**, pet **8083**, media **8084**, integration **8085**.

## Requisitos

- JDK 21
- Maven 3.9+
- Docker Desktop

```bash
mvn clean verify
mvn -pl msvc-user spring-boot:run
```

## Configuración local (Docker Compose)

Requiere archivo `.env` en la raíz (copiar desde `.env.example`).

```bash
cp .env.example .env
# Editar .env con JWT_SECRET, credenciales Postgres y MinIO

docker compose up --build -d
docker compose ps
```

Servicios levantados:

| Servicio | Puerto host | Descripción |
|----------|-------------|-------------|
| msvc-user | 9081 | Auth y usuarios |
| msvc-report | 9082 | Reportes |
| msvc-pet | 9083 | Mascotas |
| msvc-media | 9084 | Upload/presign (MinIO local) |
| msvc-integration | 9085 | Integración externa |
| postgres | 5433 | PostgreSQL compartido |
| minio | 9000 / 9001 | S3 local |

Pruebas con Postman: colección `postman/Sanos-y-Salvos-User-Pet-Report.postman_collection.json`.

Flujo mínimo: `POST /api/v1/auth/login` → `POST /api/v1/pets` → `POST /api/v1/reports`.

## Dockerfiles (IE1)

Cada microservicio HTTP tiene un Dockerfile multi-stage en `docker/`:

| Archivo | Imagen |
|---------|--------|
| `docker/Dockerfile.msvc-user` | `sanos/msvc-user:latest` |
| `docker/Dockerfile.msvc-pet` | `sanos/msvc-pet:latest` |
| `docker/Dockerfile.msvc-report` | `sanos/msvc-report:latest` |
| `docker/Dockerfile.msvc-media` | `sanos/msvc-media:latest` |
| `docker/Dockerfile.msvc-integration` | `sanos/msvc-integration:latest` |

Patrón: stage `maven:3.9-eclipse-temurin-21-alpine` para compilar, stage `eclipse-temurin:21-jre-alpine` para ejecutar el JAR.

## Docker Swarm (IE7 / IE8)

### 1. Construir imágenes locales

```bash
docker build -f docker/Dockerfile.msvc-user -t sanos/msvc-user:latest .
docker build -f docker/Dockerfile.msvc-pet -t sanos/msvc-pet:latest .
docker build -f docker/Dockerfile.msvc-report -t sanos/msvc-report:latest .
docker build -f docker/Dockerfile.msvc-media -t sanos/msvc-media:latest .
docker build -f docker/Dockerfile.msvc-integration -t sanos/msvc-integration:latest .
```

### 2. Inicializar Swarm y desplegar stack

```bash
docker swarm init
docker stack deploy -c docker-compose.swarm.yml sanos
docker service ls
```

En PowerShell, exporta variables antes del deploy:

```powershell
Get-Content .env | ForEach-Object {
  if ($_ -match '^\s*([^#][^=]+)=(.*)$') { Set-Item -Path "env:$($matches[1])" -Value $matches[2] }
}
docker stack deploy -c docker-compose.swarm.yml sanos
```

### 3. Escalar réplicas (demo IE8)

```bash
docker service scale sanos_msvc-user=3
docker service ps sanos_msvc-user
```

### 4. Añadir nodo worker (opcional)

En el manager:

```bash
docker swarm join-token worker
```

En otra máquina con Docker:

```bash
docker swarm join --token <TOKEN> <IP_MANAGER>:2377
```

### 5. Detener stack

```bash
docker stack rm sanos
```

Puertos expuestos en Swarm: **9081** user, **9082** report, **9083** pet, **9084** media, **9085** integration.

## CI/CD (IE4 / IE5 / IE6)

Workflow: `.github/workflows/ci-cd.yml`

Se dispara con push o PR a `develop`/`main`, y con `workflow_dispatch`.

| Job | Descripción |
|-----|-------------|
| **Build y tests** | `mvn verify` en todo el monorepo |
| **Empaquetar JARs Lambda** | Shade de matching, notification y analytics |
| **Desplegar en AWS** | Solo en push a `develop` (no en PR) |

El job de deploy ejecuta, de forma idempotente:

1. Crear bucket **S3** `sanos-media-prod` (si no existe)
2. Crear cola **SQS** `sanos-analytics-queue` (si no existe)
3. Crear tabla **DynamoDB** `sanos-analytics` (si no existe)
4. Build y push de imágenes a **ECR** (5 microservicios)
5. Publicar JARs Lambda en S3 y actualizar funciones
6. Force deployment de servicios **ECS** (`sanos-svc-*`)

Secrets requeridos en GitHub Actions:

- `AWS_ACCESS_KEY_ID`
- `AWS_SECRET_ACCESS_KEY`
- `AWS_ACCOUNT_ID`
- `AWS_SESSION_TOKEN` (credenciales temporales del lab AWS Academy)

## AWS — arquitectura (lab)

| Componente | Servicio AWS |
|------------|--------------|
| Microservicios HTTP | **ECS Fargate** (`sanos-cluster`) detrás de ALB |
| Entrada pública | **API Gateway** → ALB / Lambda |
| Datos transaccionales | **RDS PostgreSQL** (`sanos-postgres-primary`) |
| Fotos | **S3** `sanos-media-prod` |
| Eventos de dominio | **EventBridge** bus `sanos-events` |
| Cola asíncrona | **SQS** `sanos-analytics-queue` |
| Matching + métricas | **DynamoDB** `sanos-analytics` |
| Alertas | **SNS** |

Guía de despliegue S3 + media en ECS: [`aws/PASO-2-S3-MEDIA-ECS.md`](aws/PASO-2-S3-MEDIA-ECS.md).

## Funciones serverless (IE10 / IE13)

| Función Lambda | Módulo | Trigger | Rol |
|----------------|--------|---------|-----|
| `sanos-matching` | `msvc-matching` | EventBridge | Indexa reportes, busca coincidencias (Rekognition + S3) |
| `sanos-notification` | `msvc-notification` | EventBridge | Publica alertas en SNS |
| `sanos-analytics` | `msvc-analytics` | SQS | Persiste métricas en DynamoDB |
| `sanos-analytics-api` | `msvc-analytics` | API Gateway | `GET /analytics/stats`, `/analytics/hot-zones` |

Flujo al crear un reporte:

```text
msvc-report (ECS)
  → EventBridge (sanos-events, evento pet_reported)
    → SQS sanos-analytics-queue → Lambda sanos-analytics → DynamoDB
    → Lambda sanos-matching (paralelo)
    → Lambda sanos-notification → SNS (paralelo)
```

Prueba E2E en AWS: colección `postman/Sanos-y-Salvos-AWS.postman_collection.json` + environment `Sanos-y-Salvos-AWS.postman_environment.json`. Activar environment y ejecutar carpeta `00 - Flujo E2E AWS (IE15)`.

## Estrategia de ramas

El proyecto usa un flujo de trabajo ambientado en multiples entornos:

### Ramas principales

- `main`: Producción (codigo éstable)
- `staging`: Pre-producción (validación final antes de producción)
- `qa`: Testing(pruebas funcionales)
- `develop`: Desarrollo

### Ramas de trabajo

Todas las ramas de trabajo se crearan desde `develop`:
- `feature/nombre-feature`: nuevas funcionalidades
- `fix/nombre-bug`: corrección de errores
- `chore/nombre-tarea`: tareas técnicas

#### Ejemplos:

- `feature/pet-report`
- `fix/register-error`
- `chore/update-deps`

## Flujo de Trabajo

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
- Validación final (bugs críticos, configuración, rendimiento básico)
### 5. Producción
- Pull Request: `staging` -> `main`
- Código estable listo para deploy

## Convención de Commits

Se utilizaran **Conventional Commits** para estructurar los mensajes de Commit en Git. Esto permitira que sean faciles de leer para el Desarrollador.

### Formato
```bash
tipo: descripción
```

### Tipos
- `feat`: nueva funcionalidad
- `fix`: corrección de bug
- `chore`: tareas internas
- `docs`: documentación
- `refactor`: mejora de código

#### Ejemplos:
- `feat: add pet report creation`
- `fix: resolve authentication error`
- `docs: update git workflow`

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
- Pull Request obligatorio (recomendado)
  
## Checklist de Pull Request
- Código probado
- No rompe funcionalidades existentes
- Sigue convención de commits
- PR revisado por al menos 1 integrante
- Rama actualizada con la base

## Notas adicionales

- Los datos en H2 (perfil `dev`) **se pierden al reiniciar** el servicio.
- El bucket MinIO (`sanos-media`) se crea al primer upload en local.
- **SY-62 (notificaciones):** implementación **broadcast vía SNS**; filtrado geo-radio queda como mejora futura.
- API key demo integración: `vet-las-heras-key-2024` (header `X-Api-Key`). Seed manual en `db/data.sql`.
- Seed de institución: ejecutar `db/data.sql` en PostgreSQL reemplazando `ID_DE_USUARIO_CREADO` por un UUID de usuario registrado.

## Buenas prácticas
- No trabajar directamente en ramas principales
- Mantener PR pequeños y claros
- Hacer commits descriptivos
- Eliminar ramas después del merge
