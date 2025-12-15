package ecommerce.core.infra.security;

import ecommerce.core.domain.auth.UserPrincipal;

public interface JwtTokenProvider {
    String generateAccessToken(UserPrincipal userPrincipal) throws Exception;
    String generateRefreshToken(UserPrincipal userPrincipal) throws Exception;
    String getUsernameFromToken(String token);
    boolean validateToken(String token);
    Long getExpirationTime();
}
