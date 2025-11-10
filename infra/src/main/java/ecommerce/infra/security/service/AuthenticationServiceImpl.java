package ecommerce.infra.security.service;

import ecommerce.core.domain.auth.AuthenticationRequest;
import ecommerce.core.domain.auth.JwtToken;
import ecommerce.core.domain.auth.UserPrincipal;
import ecommerce.core.infra.security.JwtTokenProvider;
import ecommerce.core.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public JwtToken authenticate(AuthenticationRequest request) {
        log.info("Infra - Authenticating user {}", request.getUsername() + " pass: " + request.getPassword());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        log.info("Infra - start load userPrincipal");
        UserPrincipal userPrincipal  = userDetailsService.loadUserPrincipalByUsername(request.getUsername());;
        String accessToken;
        String refreshToken;
        try{
            log.info("Infra - get  userPrincipal", userPrincipal.getUsername());
            // Generate tokens
            accessToken = jwtTokenProvider.generateAccessToken(userPrincipal);
            refreshToken = jwtTokenProvider.generateRefreshToken(userPrincipal);
        }
        catch (Exception e) {
            log.error("Infra - Authentication Failed for user: {}", request.getUsername(), e);
            log.error("Error details: {}", e.getMessage());
            log.error("Error class: {}", e.getClass().getName());
            if (e.getCause() != null) {
                log.error("Cause: {}", e.getCause().getMessage());
            }
            throw new RuntimeException("Authentication failed: " + e.getMessage(), e);
        }


        log.info("Infra - return  accessToken");
        return JwtToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .build();
    }

    @Override
    public JwtToken refreshToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        UserPrincipal userPrincipal = userDetailsService.loadUserPrincipalByUsername(username);

        String newAccessToken = jwtTokenProvider.generateAccessToken(userPrincipal);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(userPrincipal);

        return JwtToken.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .tokenType("Bearer")
                .expiresIn(jwtTokenProvider.getExpirationTime())
                .build();
    }
}
