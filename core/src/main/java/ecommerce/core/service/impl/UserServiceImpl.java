package ecommerce.core.service.impl;

import ecommerce.core.domain.user.UserCoreRequest;
import ecommerce.core.domain.user.UserCoreResponse;
import ecommerce.core.infra.UserClientService;
import ecommerce.core.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserClientService userClientService;

    @Override
    public UserCoreResponse registerUser(UserCoreRequest request) {
        log.info("Registering new user: {}", request.getUsername());

        validateRegistrationRequest(request);

        return userClientService.createUser(request);
    }

    @Override
    public UserCoreResponse getUserByUsername(String username) {
        log.info("Getting user by username: {}", username);
        return userClientService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));
    }

    @Override
    public UserCoreResponse getUserById(Long id) {
        log.info("Getting user by ID: {}", id);
        return userClientService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
    }

    @Override
    public UserCoreResponse updateUser(Long id, UserCoreRequest request) {
        log.info("Updating user: {}", id);

        // Check if user exists
        userClientService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));

        return userClientService.updateUser(id, request);
    }

    @Override
    public UserCoreResponse updateCurrentUser(String username, UserCoreRequest request) {
        log.info("Updating current user: {}", username);

        // Get current user
        UserCoreResponse currentUser = userClientService.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + username));

        // Validate update request
        validateUpdateRequest(currentUser, request);

        return userClientService.updateCurrentUser(currentUser.getId(), request);
    }

    private void validateRegistrationRequest(UserCoreRequest request) {
        log.info("Validating registration request");

        if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username is required");
        }

        if (request.getPassword() == null || request.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }

        if (request.getEmail() == null || !request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Valid email is required");
        }

        // Check if username already exists
        if (userClientService.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists: " + request.getUsername());
        }

        // Check if email already exists
        if (userClientService.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists: " + request.getEmail());
        }
    }

    private void validateUpdateRequest(UserCoreResponse currentUser, UserCoreRequest request) {
        log.info("Validating update request");

        // Validate email format if provided
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                throw new IllegalArgumentException("Valid email is required");
            }

            // Check if email is being changed and if new email already exists
            if (!request.getEmail().equals(currentUser.getEmail())
                    && userClientService.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already exists: " + request.getEmail());
            }
        }

        // If changing password, verify current password first
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            if (request.getCurrentPassword() == null || request.getCurrentPassword().trim().isEmpty()) {
                throw new IllegalArgumentException("Current password is required to change password");
            }

            if (request.getPassword().length() < 6) {
                throw new IllegalArgumentException("New password must be at least 6 characters");
            }
        }
    }
}
