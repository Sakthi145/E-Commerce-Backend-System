package com.ecommerce.backend.config;

import com.ecommerce.backend.user_module.UserRepository;
import com.ecommerce.backend.user_module.models.Role;
import com.ecommerce.backend.user_module.models.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class AdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public AdminInitializer(

            UserRepository userRepository,

            PasswordEncoder passwordEncoder

    ) {

        this.userRepository = userRepository;

        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void run(String... args) {

        if (userRepository.findByEmail("admin@gmail.com").isPresent()) {

            return;

        }

        User admin = new User();

        admin.setFirstName("System");

        admin.setLastName("Administrator");

        admin.setEmail("admin@gmail.com");

        admin.setPassword(
                passwordEncoder.encode("Admin@123")
        );

        admin.setRole(Role.ADMIN);

        admin.setCreatedAt(LocalDateTime.now());

        userRepository.save(admin);

        System.out.println("Default Admin Created Successfully.");

    }

}