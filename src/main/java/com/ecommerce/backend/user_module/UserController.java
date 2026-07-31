package com.ecommerce.backend.user_module;

import com.ecommerce.backend.user_module.dto.LoginRequest;
import com.ecommerce.backend.user_module.dto.LoginResponse;
import com.ecommerce.backend.user_module.dto.RegisterRequest;
import com.ecommerce.backend.user_module.dto.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ecommerce/auth")
@Tag(
        name = "Authentication",
        description = "User registration and login APIs"
)
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }



    @Operation(summary = "Register New Customer")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed"),
            @ApiResponse(responseCode = "409", description = "Email already registered")
    })
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(

            @Valid @RequestBody RegisterRequest request

    ) {

        return new ResponseEntity<>(

                userService.register(request),

                HttpStatus.CREATED

        );

    }


    @Operation(summary = "Login User")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login successful"),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Invalid email or password")
    })

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(

            @Valid
            @RequestBody
            LoginRequest request

    ) {

        return ResponseEntity.ok(

                userService.login(request)

        );

    }

}