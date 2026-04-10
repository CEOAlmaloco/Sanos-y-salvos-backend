package com.javadiseno.sanosysalvos.user.dto;

import com.javadiseno.sanosysalvos.user.model.User;
import org.mapstruct.*;

/**
 * SY-2 - Mapper entre User y DTOs asociados.
 *
 * MapStruct lo implementa en tiempo de compilación
 * `componentModel = spring` para que se registre como bean de Spring,
 * permitiendo inyectarlo con @AutoWired.
 *
 * @author Christián Mesa
 */
@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     *  Convierte la entidad del usuario en un objeto de respuesta DTO.
     *  hashPassword se excluye ya que el DTO no tiene ese campo.
     *
     * @param user La entidad del usuario proveniente de la BD.
     * @return Un objeto UserResponseDTO con los datos correspondientes.
     */
    UserResponseDTO toResponseDTO(User user);

    /**
     *  Convierte el DTO de petición de registro en una entidad User.
     *  hashPassword se excluye ya que el service se encargara de asignarlo.
     *
     * @param registerRequestDTO El DTO con los datos de registro.
     * @return Una entidad User con los campos básicos mapeados.
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "hashPassword", ignore = true)
    @Mapping(target = "role", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    User toEntity(RegisterRequestDTO registerRequestDTO);
}
