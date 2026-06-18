# Microservicios (Java / Spring Boot)

APIs REST, JWT, Docker y BD por servicio.

## Estructura prevista

Monorepo Maven (POM padre + módulos):

```text
Sanos-y-salvos-backend/
├── pom.xml                 ← POM padre (Spring Boot 4.0.5, Java 21)
├── msvc-usuario/
├── msvc-mascota/
├── msvc-reporte/
├── msvc-media/
├── README.md
└── ...
```

| Componente | Versión |
| ------------ | ------- |
| Spring Boot | 4.0.5 |
| Java | 21 |
| Spring Cloud BOM | 2025.1.0 |

```bash
mvn clean verify
mvn -pl msvc-usuario spring-boot:run
```

Puertos por defecto: usuario **8081**, mascota **8083**, reporte **8082**, media **8084**, integration **8085**.

## Docker Swarm (IE7 / IE8)

Requiere `.env` (copiar desde `.env.example`).

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

Puertos expuestos: **9081** user, **9082** report, **9083** pet, **9084** media, **9085** integration.

## Requisitos

- JDK 17 o 21 (monorepo configurado en **21**)
- Maven o Gradle
- Docker Desktop 
## Configuración local

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

## 🔒 Protección de ramas
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
  
## 📋 Checklist de Pull Request
- Código probado
- No rompe funcionalidades existentes
- Sigue convención de commits
- PR revisado por al menos 1 integrante
- Rama actualizada con la base
  
## 🎯 Buenas prácticas
- No trabajar directamente en ramas principales
- Mantener PR pequeños y claros
- Hacer commits descriptivos
- Eliminar ramas después del merge
