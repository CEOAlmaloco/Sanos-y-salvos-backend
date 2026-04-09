package com.javadiseno.sanosysalvos.user.model;

/**
 * SY-1/SY-8 - Roles de usuario en la plataforma.
 *
 * OWNER: ciudadano que reporta mascotas perdidas o encontradas.
 * VOLUNTEER: participa en operativos de busqueda y ve zonas asignadas.
 * ADMIN: gestiona la plataforma (cambiar roles de usuario, dashboards).
 *
 * Se le asigna por defecto a cada usuario que se registre el rol de 'OWNER'.
 *
 * @author Christián Mesa
 */
public enum Role {
    OWNER,
    VOLUNTEER,
    ADMIN
}
