package com.javadiseno.sanosysalvos.user.repository;

import com.javadiseno.sanosysalvos.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

/**
 * SY-1 Repositorio de User
 *
 * Contiene los métodos para interactuar con la Base de Datos de usuarios mediante operaciones CRUD.
 * Los metodos básicos como save, findById o delete vienen de JpaRepository.
 *
 * @author Christián Mesa
 */
@Repository // Marcar como componente de la capa de persistencia.
public interface UserRepository extends JpaRepository<User, UUID> {

    /**
     * SY-2 Verificar si ya existe un usuario con ese email antes
     * de registrar uno nuevo. Es esencial para evitar la excepción de
     * constraint unique y devuelve un 409 Conflict con mensaje claro.
     *
     * @param email
     * @return true/false
     */
    boolean existsByEmail(String email);
}
