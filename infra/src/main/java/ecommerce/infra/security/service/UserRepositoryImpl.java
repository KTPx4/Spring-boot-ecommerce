package ecommerce.infra.security.service;

import ecommerce.core.domain.auth.UserPrincipal;
import ecommerce.core.infra.repository.UserRepository;
import ecommerce.infra.client.entity.User;
import ecommerce.infra.client.entity.UserRole;
import ecommerce.infra.client.repository.UserJpaRepository;
import ecommerce.infra.client.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;
    private final UserRoleJpaRepository userRoleJpaRepository;

    @Override
    public Optional<UserPrincipal> findByUsername(String username) {
        return userJpaRepository.findByUsername(username)
                .map(this::mapToUserPrincipal);
    }

    @Override
    public Optional<UserPrincipal> findById(Long id) {
        return userJpaRepository.findById(id)
                .map(this::mapToUserPrincipal);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userJpaRepository.existsByUserName(username);
    }

    private UserPrincipal mapToUserPrincipal(User user) {
        List<UserRole> userRoles = userRoleJpaRepository.findByUserId(user.getId());
        
        return UserPrincipal.builder()
                .id(user.getId())
                .username(user.getUserName())
                .password(user.getPassHash()) // Include password hash
                .email(user.getEmail())
                .fullName(user.getFullName())
                .status(user.getStatus())
                .roles(userRoles.stream()
                        .map(ur -> ur.getRole().getName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
