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

import java.util.UUID;

/**
 * SY-2 | SY-3 | SY-6 | SY-7 | SY-8 Implementación de UserService
 */
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtService jwtService;

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

    //SY-3 | SY-4
    @Override
    @Transactional(readOnly = true)
    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {

        User user = userRepository.findUserByEmail(loginRequestDTO.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(loginRequestDTO.getPassword(), user.getHashPassword())){
            throw new InvalidCredentialsException();
        }

        String token = jwtService.generateToken(user);

        return LoginResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    //SY-6
    @Override
    @Transactional(readOnly = true)
    public UserResponseDTO getUserProfile(String email){
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO updateUserProfile(String email, UpdateProfileRequestDTO updateProfileRequestDTO){
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));


        userMapper.updateEntityFromDTO(updateProfileRequestDTO, user);

        return userMapper.toResponseDTO(user);
    }

    @Override
    @Transactional
    public UserResponseDTO changeUserRole(String adminEmail, UUID targetId, ChangeRoleRequestDTO changeRoleRequestDTO){

        User admin = userRepository.findUserByEmail(adminEmail)
                .orElseThrow(() -> new UserNotFoundException(adminEmail));

        if(admin.getId().equals(targetId)){
            throw new SelfRoleChangeException();
        }

        User target = userRepository.findById(targetId)
                .orElseThrow(() -> new UserNotFoundException(targetId.toString()));

        target.setRole(Role.valueOf(changeRoleRequestDTO.getRole()));

        return userMapper.toResponseDTO(userRepository.save(target));
    }

}
