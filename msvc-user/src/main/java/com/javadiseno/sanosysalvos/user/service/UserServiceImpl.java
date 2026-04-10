package com.javadiseno.sanosysalvos.user.service;

import com.javadiseno.sanosysalvos.user.dto.RegisterRequestDTO;
import com.javadiseno.sanosysalvos.user.dto.UserMapper;
import com.javadiseno.sanosysalvos.user.dto.UserResponseDTO;
import com.javadiseno.sanosysalvos.user.exception.EmailAlreadyExistsException;
import com.javadiseno.sanosysalvos.user.exception.PasswordMismatchException;
import com.javadiseno.sanosysalvos.user.model.Role;
import com.javadiseno.sanosysalvos.user.model.User;
import com.javadiseno.sanosysalvos.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * SY-2 - Implementación del UserService
 *
 * @author Christián Mesa
 */
@Service // Marcar la clase UserServiceImpl como un componente de la capa de servicio.
@RequiredArgsConstructor // Genera automáticamente un constructor en tiempo de compilación para todos los campos final.
public class UserServiceImpl implements UserService{

    // Variables Inmutables

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDTO register(RegisterRequestDTO registerRequestDTO) {

        /**
         * 1. Validación de contraseñas: password == confirmPassword:
         *    - Se realiza aqui para centralizar los errores.
         *    - Devuelve un estado HTTP 400 Bad Request con un mensaje claro.
         */

        if (!registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword())){
            throw new PasswordMismatchException();
        }

        /**
         * 2. Verifica si el email ingresado existe antes de persistir.
         *    - Devuelve un estado HTTP 409 Conflict con los detalles de error correspondientes.
         *    - Controla el constraint unique del campo email en la BD.
         */

        if(userRepository.existsByEmail(registerRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException(registerRequestDTO.getEmail());
        }

        /**
         * 3. Convierte el RegisterRequestDTO en una entidad User.
         */

        User user = userMapper.toEntity(registerRequestDTO);

        /**
         * 4. Hashear la contraseña con BCrypt.
         *    - Cada llamada produce un hash distinto independiente de que la contraseña sea la misma.
         */

        user.setHashPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        /**
         * 5. Asigna el rol OWNER al usuario por defecto.
         */
        user.setRole(Role.OWNER);

        /**
         * 6. El usuario se guarda en la BD. Antes de persistir, @PrePersist asigna el UUID del usuario.
         */
        User savedUser = userRepository.save(user);

        /**
         * 7. Devolver el DTO de respuesta sin algún dato sensible.
         */
        return userMapper.toResponseDTO(savedUser);
    }
}
