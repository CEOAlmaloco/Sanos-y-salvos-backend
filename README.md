# Microservicios (Java / Spring Boot)

Monorepo para APIs REST, JWT, Docker y BD por servicio.

## Estructura prevista

```text
microservices/
  docs/
    diagrams/          # diagramas (Mermaid, draw.io export, etc.)
  api-gateway/         # (generar con Spring Initializr)
  auth-service/
  ...                  # un directorio por microservicio
  docker-compose.yml   # (añadir cuando tengas Dockerfiles)
```

## Requisitos

- JDK 17 o 21 (recomendado)
- Maven o Gradle (según lo que elijas en Spring Initializr)
- Docker Desktop (opcional, para orquestar servicios y BDs)

## Configuración local

1. Copia las variables de entorno:

   ```powershell
   Copy-Item .env.example .env
   ```

2. Edita `.env` con URLs y secretos de tu máquina (no lo subas a Git).

## Git y ramas

- Trabajo en ramas `feature/...` y fusión a `main` (o `develop`) vía Pull Request.
- Guía detallada de arquitectura e Initializr: `../GUIA_MICROSERVICIOS_GITHUB_SPRING.md`

## Próximos pasos

1. Crear en GitHub el repositorio vacío y conectar `git remote add origin ...`.
2. Generar cada servicio en [start.spring.io](https://start.spring.io) y colocarlo en su carpeta.
3. Añadir `docker-compose.yml` y Dockerfiles por servicio cuando las apps compilen.
