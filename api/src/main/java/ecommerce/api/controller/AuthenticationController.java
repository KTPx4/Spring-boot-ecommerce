package ecommerce.api.controller;

import ecommerce.api.dto.auth.*;
import ecommerce.core.domain.auth.AuthenticationRequest;
import ecommerce.core.domain.auth.JwtToken;
import ecommerce.core.domain.user.UserCoreRequest;
import ecommerce.core.domain.user.UserCoreResponse;
import ecommerce.core.service.AuthenticationService;
import ecommerce.core.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication management APIs")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/login")
    @Operation(summary = "User Login", description = "Authenticate user with username/password and receive JWT access & refresh tokens")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login successful, tokens returned"),
            @ApiResponse(responseCode = "401", description = "Invalid credentials")
    })
    public ResponseEntity<LoginResponse> login(
            @Parameter(description = "Login credentials", required = true) @RequestBody LoginRequest request) {
        log.info("Login request for user: {}", request.getUsername());

        AuthenticationRequest authRequest = AuthenticationRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .build();

        JwtToken jwtToken = authenticationService.authenticate(authRequest);

        LoginResponse response = LoginResponse.builder()
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .tokenType(jwtToken.getTokenType())
                .expiresIn(jwtToken.getExpiresIn())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh Access Token", description = "Generate new access token using a valid refresh token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token refreshed successfully"),
            @ApiResponse(responseCode = "401", description = "Invalid or expired refresh token")
    })
    public ResponseEntity<LoginResponse> refreshToken(
            @Parameter(description = "Refresh token", required = true) @RequestBody RefreshTokenRequest request) {
        log.info("Refresh token request");

        JwtToken jwtToken = authenticationService.refreshToken(request.getRefreshToken());

        LoginResponse response = LoginResponse.builder()
                .accessToken(jwtToken.getAccessToken())
                .refreshToken(jwtToken.getRefreshToken())
                .tokenType(jwtToken.getTokenType())
                .expiresIn(jwtToken.getExpiresIn())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/test")
    @Operation(summary = "Test Authentication", description = "Verify if the current JWT token is valid. Requires authentication.", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token is valid"),
            @ApiResponse(responseCode = "401", description = "Token is invalid or missing")
    })
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Authentication successful!");
    }

    @PostMapping("/register")
    @Operation(summary = "User Registration", description = "Create a new user account with USER role by default. Username and email must be unique.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User registered successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input or username/email already exists")
    })
    public ResponseEntity<RegisterResponse> register(
            @Parameter(description = "User registration details", required = true) @RequestBody RegisterRequest request) {
        log.info("Register request for user: {}", request.getUsername());

        UserCoreRequest userRequest = UserCoreRequest.builder()
                .username(request.getUsername())
                .password(request.getPassword())
                .email(request.getEmail())
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .birthDay(request.getBirthDay())
                .gender(request.getGender())
                .build();

        UserCoreResponse userResponse = userService.registerUser(userRequest);

        RegisterResponse response = RegisterResponse.builder()
                .id(userResponse.getId())
                .username(userResponse.getUsername())
                .email(userResponse.getEmail())
                .fullName(userResponse.getFullName())
                .phone(userResponse.getPhone())
                .createdAt(userResponse.getCreatedAt())
                .build();

        return ResponseEntity.ok(response);
    }
}
