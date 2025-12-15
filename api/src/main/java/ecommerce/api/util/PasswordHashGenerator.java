package ecommerce.api.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utility class to generate BCrypt password hashes
 * Run this class to generate hashed passwords for test data
 */
public class PasswordHashGenerator {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        // Generate hash for "password123"
        String password = "password123";
        String hashedPassword = encoder.encode(password);
        
        System.out.println("=== Password Hash Generator ===");
        System.out.println("Original password: " + password);
        System.out.println("Hashed password: " + hashedPassword);
        System.out.println("\nCopy this hash to your SQL insert statement:");
        System.out.println("pass_hash = '" + hashedPassword + "'");
        
        // Verify hash
        boolean matches = encoder.matches(password, hashedPassword);
        System.out.println("\nVerification: " + (matches ? "✓ Password matches hash" : "✗ Password does not match"));
    }
}
