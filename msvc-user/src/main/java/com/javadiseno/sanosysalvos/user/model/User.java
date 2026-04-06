package com.javadiseno.sanosysalvos.user.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * SY-1 - Crear Entidad User
 *
 * Esta clase representa a cualquier usuario registrado en la plataforma.
 * El rol determina qué puede hacer, por ejemplo: DUENO puede reportar mascotas,
 * un VOLUNTARIO puede ver zonas asignadas, un ADMIN administra la plataforma.
 *
 * Estructura:
 *
 * - ID: Identificador de tipo UUID generado por java antes de persistir. De esta forma no se depende de la Base de Datos.
 * - Email: Unico a nivel de Base de Datos.
 * - Password: Nunca se almacena de manera clara. Solo se guarda formateada en hash por BCryptPasswordEncoder
 *   en la capa 'service'.
 * - Auditoría minima: Se usara createdAt y updateAt mediante JPA Auditing para monitoreo.
 *
 * @author Christián Mesa
 */

@EntityListeners(AuditingEntityListener.class) // Activa @CreatedDate y @LastModifiedDate
@Entity // Mapear la clase a una tabla de BD.
@Table(name = "user") // Definir nombre de la tabla.
@Getter // Genera metodos getter para cada campo de la clase, permitiendo obtener información del objeto.
@Setter // Genera metodos setter para cada campo de la clase, permitiendo cambiar o establecer informacion del objeto fuera de la clase.
@NoArgsConstructor // Genera un constructor sin argumentos para la clase User.
@AllArgsConstructor // Genera un constructor con argumentos para cada campo de la clase User.
public class User {

    // -- Identificador ---------------------

    @Id //Marca el campo 'id' como clave primaria de la tabla 'user'.
    private UUID id;

    /**
     * Se asigna el UUID antes de persistir para no depender de la BD.
     */
    @PrePersist // Marca el metodo 'assignId' para que se ejecute automáticamente antes que se haga un INSERT por primera vez en la BD.
    public void assignId(){
        if(this.id == null){
            this.id = UUID.randomUUID();
        }
    }

    // -- Datos de Acceso ---------------------

    @Column(name = "email", nullable = false, unique = true, length = 100) // Configurar propiedades de la columna 'email' (no nulo y único).
    private String email;

    /**
     * Hash BCrypt. Nunca se expone en DTOs de respuesta.
     */
    @Column(name = "hash_password", nullable = false) // Configurar propiedades de la columna 'hash_password' (no nulo).
    private String hashPassword;

    // -- Datos Personales ---------------------

    @Column(name = "name", nullable = false, length = 50) // Configurar propiedades de la columna 'name' (no nulo, longitud).
    private String name; // Nombres del usuario

    @Column(name = "last_name", nullable = false, length = 50) // Configurar propiedades de la columna 'last_name' (no nulo, longitud).
    private String lastName; // Apellidos del usuario

    @Column(name = "phone", length = 20) // Configurar propiedades de la columna 'phone' (no nulo).
    private String phone; // Teléfono del usuario

    // -- Rol ---------------------

    /**
     * Rol de usuario en la plataforma.
     *
     * Reglas de negocio (SY-8)
     *  - El rol asignado por defecto siempre sera 'OWNER'
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20) // Configurar propiedades de la columna 'last_name' (no nulo, longitud).
    private Role role;

    // -- Auditoria ---------------------

    /**
     * Fecha de Creación: la asigna JPA Auditing al primer persist.
     */
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false) // Configurar propiedades de la columna 'created_at' (no nulo y no actualizable).
    private LocalDateTime createdAt;

    /**
     * Fecha de Última Modificación: se actualiza en cada update.
     */
    @LastModifiedDate
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
