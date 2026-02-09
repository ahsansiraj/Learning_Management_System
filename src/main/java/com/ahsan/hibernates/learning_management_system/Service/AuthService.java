package com.ahsan.hibernates.learning_management_system.Service;

import com.ahsan.hibernates.learning_management_system.DTO.Request.LoginRequest;
import com.ahsan.hibernates.learning_management_system.DTO.Request.RegisterRequest;
import com.ahsan.hibernates.learning_management_system.DTO.Response.LoginResponse;
import com.ahsan.hibernates.learning_management_system.DTO.Response.RegisterResponse;
import com.ahsan.hibernates.learning_management_system.Entity.User;
import com.ahsan.hibernates.learning_management_system.Exception.EmailAlreadyExistsException;
import com.ahsan.hibernates.learning_management_system.Repository.UserRepository;
import com.ahsan.hibernates.learning_management_system.Security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        User savedUser = userRepository.save(user);

        return RegisterResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .message("User registered successfully")
                .build();
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow();

        String token = jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .email(user.getEmail())
                .expiresIn(jwtService.getExpirationTime())
                .build();
    }
}