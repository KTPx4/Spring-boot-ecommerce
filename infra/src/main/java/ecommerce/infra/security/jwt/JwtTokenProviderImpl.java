package ecommerce.infra.security.jwt;

import ecommerce.core.domain.auth.UserPrincipal;
import ecommerce.core.infra.security.JwtTokenProvider;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {

    private final SecretKey secretKey;
    private final Long jwtExpiration;
    private final Long refreshExpiration;

    public JwtTokenProviderImpl(
            @Value("${jwt.secret:abcdefghijklmnopqrstuvxyz1234567890abcd}") String jwtSecretProperty,
            @Value("${jwt.expiration}") Long jwtExpiration,
            @Value("${jwt.refresh-expiration}") Long refreshExpiration
    ) {

        byte[] keyBytes = Decoders.BASE64.decode(jwtSecretProperty);

        this.secretKey = Keys.hmacShaKeyFor(keyBytes);

        this.jwtExpiration = jwtExpiration;
        this.refreshExpiration = refreshExpiration;
    }



    @Override
    public String generateAccessToken(UserPrincipal userPrincipal) throws  Exception {
        return generateToken(userPrincipal, jwtExpiration);
    }

    @Override
    public String generateRefreshToken(UserPrincipal userPrincipal) throws Exception{
        return generateToken(userPrincipal, refreshExpiration);
    }

    private String generateToken(UserPrincipal userPrincipal, Long expiration) throws Exception{
        log.info("Start generateToken");
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        log.info("Start generateToken - 1");

        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userPrincipal.getId());
        claims.put("email", userPrincipal.getEmail());
        claims.put("fullName", userPrincipal.getFullName());
        claims.put("roles", userPrincipal.getRoles());
        log.info("Start generateToken - 2");
        String token = "";
        try{
            token = Jwts.builder()

                    .claim("userName", userPrincipal.getUsername())

                    .claim("type", "Authorization")

                    .signWith(secretKey, SignatureAlgorithm.HS512)
                    .compact();


            log.info("return generateToken {}", token);
        }
        catch (Exception e){
            log.error("Error while generating token", e);

            e.printStackTrace();
        }


        return token;
    }

    @Override
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty");
        }
        return false;
    }

    @Override
    public Long getExpirationTime() {
        return jwtExpiration;
    }
}
