package ecommerce.core.service;

import ecommerce.core.domain.auth.AuthenticationRequest;
import ecommerce.core.domain.auth.JwtToken;

public interface AuthenticationService {
    JwtToken authenticate(AuthenticationRequest request);
    JwtToken refreshToken(String refreshToken) throws Exception;
}
