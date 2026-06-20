package com.javadiseno.sanosysalvos.media.dtos;

/**
 * Respuesta tras subir un archivo al bucket.
 *
 * @param bucket      nombre del bucket S3/MinIO
 * @param objectKey   clave del objeto dentro del bucket
 * @param contentType tipo MIME (sirve para identificar el tipo de archivo)guardado
 * @param sizeBytes   tamaño del archivo subido
 * @param readUrl     URL GET prefirmada: el backend ya metio en el enlace una firma temporal (parametros cmo
 *                    X-Amz-Signature) Entornces con esa URL se puede leer el objeto sin mandar claves de MinIO/S3 al cliente
 *                    la autenticacion va dentro de la URL. Caduca en unos pocos minutos u horas que se defina el 
 *                    servicio (no es un link publico para siempre)
 */
public record MediaUploadResponse(
        String bucket,
        String objectKey,
        String contentType,
        long sizeBytes,
        String readUrl
) {}
