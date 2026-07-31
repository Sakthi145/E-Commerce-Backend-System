package com.ecommerce.backend.user_module;

import com.ecommerce.backend.exceptions.InvalidCredentialsException;
import com.ecommerce.backend.exceptions.UserAlreadyExistsException;
import com.ecommerce.backend.exceptions.UserNotFoundException;
import com.ecommerce.backend.security.JwtService;
import com.ecommerce.backend.user_module.dto.LoginRequest;
import com.ecommerce.backend.user_module.dto.LoginResponse;
import com.ecommerce.backend.user_module.dto.RegisterRequest;
import com.ecommerce.backend.user_module.dto.UserResponse;
import com.ecommerce.backend.user_module.models.Role;
import com.ecommerce.backend.user_module.models.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(

            UserRepository userRepository,

            PasswordEncoder passwordEncoder,

            JwtService jwtService

    ) {

        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;

        this.jwtService = jwtService;

    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())

                .orElseThrow(() ->

                        new UserNotFoundException(
                                "User not found."
                        )
                );

        if (!passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        )) {

            throw new InvalidCredentialsException(
                    "Invalid email or password."
            );

        }

        String token = jwtService.generateToken(
                user.getEmail()
        );

        return new LoginResponse(token);

    }

    public UserResponse register(RegisterRequest request) {

        Optional<User> existingUser =
                userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException(
                    "Email already registered."
            );
        }

        User user = new User();

        user.setFirstName(request.getFirstName());

        user.setLastName(request.getLastName());

        user.setEmail(request.getEmail());

        user.setPassword( passwordEncoder.encode(

                request.getPassword()

        ));

        user.setRole(Role.CUSTOMER);

        user.setCreatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        return mapToResponse(savedUser);

    }

    private UserResponse mapToResponse(User user) {

        return new UserResponse(

                user.getId(),

                user.getFirstName(),

                user.getLastName(),

                user.getEmail(),

                user.getRole(),

                user.getCreatedAt()

        );

    }

}