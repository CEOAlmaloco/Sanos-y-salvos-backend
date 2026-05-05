-- ══════════════════════════════════════════════════════════
-- msvc-integration — Dato de prueba (ejecución manual)
--
-- QUÉ CREA ESTE SCRIPT:
--   Inserta 1 institucion externa ficticia en la tabla
--   integration_schema.institution_api_keys, que representan
--   los actores externos autorizados para enviar reportes de
--   mascotas a Sanos y Salvos via Integration Service:
--
--   1. Veterinaria Las Heras
--      API key: vet-las-heras-key-2024
--      Usar en Postman → Header: X-Api-Key: vet-las-heras-key-2024
--
--
-- INSTRUCCIONES ANTES DE EJECUTAR:
--   1. Registrar un usuario en msvc-user:
--      POST /api/v1/auth/register
--   2. Copiar el campo "id" de la respuesta (UUID del usuario)
--   3. Reemplazar ID_DE_USUARIO_CREADO por ese UUID en el INSERT
--
-- NOTA: Este script NO se ejecuta automáticamente al arrancar
--   la aplicación. Debe ejecutarse manualmente una sola vez.
-- ══════════════════════════════════════════════════════════

INSERT INTO integrations_schema.institution_api_keys (id, institution_name, api_key, user_id, created_at)
VALUES (
           '11111111-1111-1111-1111-111111111111',
           'Veterinaria Las Heras',
           'vet-las-heras-key-2024',
           'ID_DE_USUARIO_CREADO',
           CURRENT_TIMESTAMP
       );