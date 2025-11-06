package ecommerce.api.controller;

import ecommerce.api.dto.auth.LoginRequest;
import ecommerce.api.dto.auth.LoginResponse;
import ecommerce.api.dto.auth.RefreshTokenRequest;
import ecommerce.core.domain.auth.AuthenticationRequest;
import ecommerce.core.domain.auth.JwtToken;
import ecommerce.core.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
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

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Authenticate user and return JWT tokens")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
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
    @Operation(summary = "Refresh Token", description = "Refresh access token using refresh token")
    public ResponseEntity<LoginResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
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
    @Operation(summary = "Test Authentication", description = "Test if user is authenticated")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Authentication successful!");
    }
}
