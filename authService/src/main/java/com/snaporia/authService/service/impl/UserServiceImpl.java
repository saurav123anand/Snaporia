package com.snaporia.authService.service.impl;

import com.snaporia.authService.dto.RegisterUserRequest;
import com.snaporia.authService.dto.RegisterResponse;
import com.snaporia.authService.entity.User;
import com.snaporia.authService.exception.ResourceAlreadyExistsException;
import com.snaporia.authService.repository.UserRepository;
import com.snaporia.authService.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    @Override
    public RegisterResponse registerUser(RegisterUserRequest request) throws ResourceAlreadyExistsException {
        if (repository.findByUsername(
                request.getUsername()).isPresent()) {
            throw new ResourceAlreadyExistsException("User already exists");
        }
        User user = User.builder()
                .username(request.getUsername())
                .password(
                        encoder.encode(request.getPassword()))
                .roles(request.getRoles())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .middleName(request.getMiddleName())
                .profilePicture(request.getProfilePicture())
                .bio(request.getBio())
                .build();
        repository.save(user);
        return RegisterResponse.builder().
                message("User registered successfully").
                status("success").
                username(request.getUsername()).
                role(request.getRoles().stream().map(Enum::name).collect(Collectors.toSet())).
                timestamp(LocalDateTime.now()).
                build();

    }
}
