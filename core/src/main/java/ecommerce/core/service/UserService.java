package ecommerce.core.service;

import ecommerce.core.domain.user.UserCoreRequest;
import ecommerce.core.domain.user.UserCoreResponse;

public interface UserService {
    UserCoreResponse registerUser(UserCoreRequest request);

    UserCoreResponse getUserByUsername(String username);

    UserCoreResponse getUserById(Long id);

    UserCoreResponse updateUser(Long id, UserCoreRequest request);

    UserCoreResponse updateCurrentUser(String username, UserCoreRequest request);
}
