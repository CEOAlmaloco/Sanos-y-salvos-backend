package com.javadiseno.sanosysalvos.user.service;

import com.javadiseno.sanosysalvos.user.dto.*;
import com.javadiseno.sanosysalvos.user.exception.*;
import com.javadiseno.sanosysalvos.user.model.Role;
import com.javadiseno.sanosysalvos.user.model.User;
import com.javadiseno.sanosysalvos.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * SY-2 | SY-3 Implementación de UserService
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    //SY-2
    @Override
    @Transactional
    public UserResponseDTO register(RegisterRequestDTO registerRequestDTO) {

        if (!registerRequestDTO.getPassword().equals(registerRequestDTO.getConfirmPassword())){
            throw new PasswordMismatchException();
        }

        if(userRepository.existsByEmail(registerRequestDTO.getEmail())){
            throw new EmailAlreadyExistsException(registerRequestDTO.getEmail());
        }

        User user = userMapper.toEntity(registerRequestDTO);

        user.setHashPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));

        user.setRole(Role.OWNER);

        User savedUser = userRepository.save(user);

        return userMapper.toResponseDTO(savedUser);
    }

    //SY-3
    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        User user = userRepository.findByEmail(loginRequestDTO.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getHashPassword())){
            throw new InvalidCredentialsException();
        }

        return LoginResponseDTO.builder()
                .token("Futuro Token")
                .tokenType("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
