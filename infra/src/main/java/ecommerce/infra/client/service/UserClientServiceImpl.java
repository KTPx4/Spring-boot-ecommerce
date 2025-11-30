package ecommerce.infra.client.service;

import ecommerce.core.domain.user.UserCoreRequest;
import ecommerce.core.domain.user.UserCoreResponse;
import ecommerce.core.infra.UserClientService;
import ecommerce.infra.client.entity.Role;
import ecommerce.infra.client.entity.User;
import ecommerce.infra.client.entity.UserRole;
import ecommerce.infra.client.mapper.UserInfraMapper;
import ecommerce.infra.client.repository.RoleJpaRepository;
import ecommerce.infra.client.repository.UserJpaRepository;
import ecommerce.infra.client.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserClientServiceImpl implements UserClientService {

    private final UserJpaRepository userRepository;
    private final RoleJpaRepository roleRepository;
    private final UserRoleJpaRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public UserCoreResponse createUser(UserCoreRequest request) {
        log.info("infra - start createUser: {}", request.getUsername());

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Create user entity
        User user = UserInfraMapper.toEntity(request, encodedPassword);
        User savedUser = userRepository.save(user);

        // Assign default role (USER)
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Default USER role not found"));

        UserRole userRoleEntity = new UserRole();
        userRoleEntity.setUser(savedUser);
        userRoleEntity.setRole(userRole);
        userRoleRepository.save(userRoleEntity);

        // Reload user with roles
        User userWithRoles = userRepository.findById(savedUser.getId())
                .orElseThrow(() -> new IllegalStateException("User not found after creation"));

        log.info("infra - end createUser");
        return UserInfraMapper.toUserCoreResponse(userWithRoles);
    }

    @Override
    public Optional<UserCoreResponse> findByUsername(String username) {
        log.info("infra - findByUsername: {}", username);
        return userRepository.findByUserName(username)
                .map(UserInfraMapper::toUserCoreResponse);
    }

    @Override
    public Optional<UserCoreResponse> findByEmail(String email) {
        log.info("infra - findByEmail: {}", email);
        return userRepository.findByEmail(email)
                .map(UserInfraMapper::toUserCoreResponse);
    }

    @Override
    public Optional<UserCoreResponse> findById(Long id) {
        log.info("infra - findById: {}", id);
        return userRepository.findById(id)
                .map(UserInfraMapper::toUserCoreResponse);
    }

    @Override
    @Transactional
    public UserCoreResponse updateUser(Long id, UserCoreRequest request) {
        log.info("infra - start updateUser: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update fields
        existingUser.setEmail(request.getEmail());
        existingUser.setFullName(request.getFullName());
        existingUser.setPhone(request.getPhone());
        existingUser.setBirthDay(request.getBirthDay());
        existingUser.setGender(request.getGender());

        // Update password if provided
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            existingUser.setPassHash(passwordEncoder.encode(request.getPassword()));
        }

        User updatedUser = userRepository.save(existingUser);

        log.info("infra - end updateUser");
        return UserInfraMapper.toUserCoreResponse(updatedUser);
    }

    @Override
    @Transactional
    public UserCoreResponse updateCurrentUser(Long id, UserCoreRequest request) {
        log.info("infra - start updateCurrentUser: {}", id);

        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Verify current password if trying to change password
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            if (request.getCurrentPassword() == null ||
                    !passwordEncoder.matches(request.getCurrentPassword(), existingUser.getPassHash())) {
                throw new IllegalArgumentException("Current password is incorrect");
            }
            // Update to new password
            existingUser.setPassHash(passwordEncoder.encode(request.getPassword()));
        }

        // Update other fields if provided
        if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
            existingUser.setEmail(request.getEmail());
        }
        if (request.getFullName() != null && !request.getFullName().trim().isEmpty()) {
            existingUser.setFullName(request.getFullName());
        }
        if (request.getPhone() != null) {
            existingUser.setPhone(request.getPhone());
        }
        if (request.getBirthDay() != null) {
            existingUser.setBirthDay(request.getBirthDay());
        }
        if (request.getGender() != null) {
            existingUser.setGender(request.getGender());
        }
        if (request.getImgUrl() != null) {
            existingUser.setImgUrl(request.getImgUrl());
        }

        User updatedUser = userRepository.save(existingUser);

        log.info("infra - end updateCurrentUser");
        return UserInfraMapper.toUserCoreResponse(updatedUser);
    }

    @Override
    public boolean existsByUsername(String username) {
        log.info("infra - existsByUsername: {}", username);
        return userRepository.existsByUserName(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        log.info("infra - existsByEmail: {}", email);
        return userRepository.existsByEmail(email);
    }
}
