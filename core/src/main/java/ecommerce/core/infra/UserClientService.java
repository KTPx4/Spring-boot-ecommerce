package ecommerce.core.infra;

import ecommerce.core.domain.user.UserCoreRequest;
import ecommerce.core.domain.user.UserCoreResponse;

import java.util.Optional;

public interface UserClientService {
    UserCoreResponse createUser(UserCoreRequest request);

    Optional<UserCoreResponse> findByUsername(String username);

    Optional<UserCoreResponse> findByEmail(String email);

    Optional<UserCoreResponse> findById(Long id);

    UserCoreResponse updateUser(Long id, UserCoreRequest request);

    UserCoreResponse updateCurrentUser(Long id, UserCoreRequest request);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
