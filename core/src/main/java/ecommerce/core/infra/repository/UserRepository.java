package ecommerce.core.infra.repository;

import ecommerce.core.domain.auth.UserPrincipal;

import java.util.Optional;

public interface UserRepository {
    Optional<UserPrincipal> findByUsername(String username);
    Optional<UserPrincipal> findById(Long id);
    boolean existsByUsername(String username);
}
