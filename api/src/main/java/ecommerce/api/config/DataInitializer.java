package ecommerce.api.config;

import ecommerce.infra.client.user.entity.Role;
import ecommerce.infra.client.user.entity.User;
import ecommerce.infra.client.user.entity.UserRole;
import ecommerce.infra.client.user.repository.RoleJpaRepository;
import ecommerce.infra.client.user.repository.UserJpaRepository;
import ecommerce.infra.client.user.repository.UserRoleJpaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

/**
 * Component to initialize default data when application starts
 * Creates default roles and admin account if they don't exist
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements ApplicationRunner {

    private final UserJpaRepository userRepository;
    private final RoleJpaRepository roleRepository;
    private final UserRoleJpaRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        log.info("Starting data initialization...");
        
        // Initialize default roles
        initializeRoles();
        
        // Initialize admin account
        initializeAdminAccount();
        
        log.info("Data initialization completed successfully");
    }

    /**
     * Create default roles if they don't exist
     */
    private void initializeRoles() {
        createRoleIfNotExists("USER", "Default user role");
        createRoleIfNotExists("ADMIN", "Administrator role");
        createRoleIfNotExists("MODERATOR", "Moderator role");
    }

    /**
     * Create a role if it doesn't exist
     */
    private void createRoleIfNotExists(String roleName, String description) {
        if (!roleRepository.existsByName(roleName)) {
            Role role = new Role();
            role.setName(roleName);
            role.setDescription(description);
            role.setUserRoles(new HashSet<>());
            roleRepository.save(role);
            log.info("Created role: {}", roleName);
        } else {
            log.debug("Role already exists: {}", roleName);
        }
    }

    /**
     * Create default admin account if no admin user exists
     */
    private void initializeAdminAccount() {
        // Check if admin user already exists
        if (userRepository.existsByUserName("admin")) {
            log.debug("Admin account already exists");
            return;
        }

        // Get ADMIN role
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new RuntimeException("ADMIN role not found. Please ensure roles are initialized first."));

        // Create admin user
        User adminUser = new User();
        adminUser.setUserName("admin");
        adminUser.setPassHash(passwordEncoder.encode("admin"));
        adminUser.setFullName("System Administrator");
        adminUser.setEmail("admin@example.com");
        adminUser.setStatus(1);
        adminUser.setUserRoles(new HashSet<>());
        
        User savedUser = userRepository.save(adminUser);
        log.info("Created admin user: {}", adminUser.getUserName());

        // Assign ADMIN role to admin user
        UserRole userRole = new UserRole();
        userRole.setUser(savedUser);
        userRole.setRole(adminRole);
        userRoleRepository.save(userRole);
        log.info("Assigned ADMIN role to admin user");

        log.warn("⚠️  Default admin account created with username: 'admin' and password: 'admin'");
        log.warn("⚠️  Please change the default password after first login for security!");
    }
}
