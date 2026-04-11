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

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

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
}
