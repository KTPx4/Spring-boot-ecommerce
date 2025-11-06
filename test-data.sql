-- Script tạo dữ liệu test cho JWT Authentication
-- Password: "password123" được hash với BCrypt

-- 1. Tạo roles
INSERT INTO roles
    (name, description, created_at, updated_at)
VALUES
    ('ADMIN', 'Administrator with full access', NOW(), NOW()),
    ('USER', 'Regular user with limited access', NOW(), NOW()),
    ('MANAGER', 'Manager with moderate access', NOW(), NOW())
ON CONFLICT
(name) DO NOTHING;

-- 2. Tạo users test
-- Password cho tất cả user: "password123"
-- BCrypt hash: $2a$10$YourHashedPasswordHere (cần generate thực tế)
INSERT INTO users
    (user_name, pass_hash, full_name, phone, email, birth_day, img_url, gender, status, created_at, updated_at)
VALUES
    (
        'admin',
        '$2a$10$qNGqZQwvZ7YxGGxJ0LY5WeN4X8Z5iH8dQPxN3tYGJQN8X8Z5iH8dQP',
        'Admin User',
        '0123456789',
        'admin@example.com',
        '1990-01-01',
        'https://example.com/avatar-admin.jpg',
        'Male',
        1,
        NOW(),
        NOW()
    ),
    (
        'user1',
        '$2a$10$qNGqZQwvZ7YxGGxJ0LY5WeN4X8Z5iH8dQPxN3tYGJQN8X8Z5iH8dQP',
        'John Doe',
        '0987654321',
        'john@example.com',
        '1995-05-15',
        'https://example.com/avatar-john.jpg',
        'Male',
        1,
        NOW(),
        NOW()
    ),
    (
        'manager1',
        '$2a$10$qNGqZQwvZ7YxGGxJ0LY5WeN4X8Z5iH8dQPxN3tYGJQN8X8Z5iH8dQP',
        'Jane Manager',
        '0912345678',
        'jane@example.com',
        '1992-08-20',
        'https://example.com/avatar-jane.jpg',
        'Female',
        1,
        NOW(),
        NOW()
    ),
    (
        'inactive_user',
        '$2a$10$qNGqZQwvZ7YxGGxJ0LY5WeN4X8Z5iH8dQPxN3tYGJQN8X8Z5iH8dQP',
        'Inactive User',
        '0999999999',
        'inactive@example.com',
        '1988-12-31',
        'https://example.com/avatar-inactive.jpg',
        'Other',
        0,
        NOW(),
        NOW()
    )
ON CONFLICT
(user_name) DO NOTHING;

-- 3. Gán roles cho users
-- Admin có cả ADMIN và USER role
INSERT INTO user_roles
    (user_id, role_id, created_at, updated_at)
SELECT u.id, r.id, NOW(), NOW()
FROM users u
CROSS JOIN roles r
WHERE u.user_name = 'admin' AND r.name IN ('ADMIN', 'USER')
ON CONFLICT DO NOTHING;

-- User1 chỉ có USER role
INSERT INTO user_roles
    (user_id, role_id, created_at, updated_at)
SELECT u.id, r.id, NOW(), NOW()
FROM users u
CROSS JOIN roles r
WHERE u.user_name = 'user1' AND r.name = 'USER'
ON CONFLICT DO NOTHING;

-- Manager có USER và MANAGER role
INSERT INTO user_roles
    (user_id, role_id, created_at, updated_at)
SELECT u.id, r.id, NOW(), NOW()
FROM users u
CROSS JOIN roles r
WHERE u.user_name = 'manager1' AND r.name IN ('MANAGER', 'USER')
ON CONFLICT DO NOTHING;

-- Inactive user có USER role nhưng status = 0
INSERT INTO user_roles
    (user_id, role_id, created_at, updated_at)
SELECT u.id, r.id, NOW(), NOW()
FROM users u
CROSS JOIN roles r
WHERE u.user_name = 'inactive_user' AND r.name = 'USER'
ON CONFLICT DO NOTHING;

-- 4. Query để xem kết quả
SELECT
    u.user_name,
    u.full_name,
    u.email,
    u.status,
    STRING_AGG(r.name, ', ') as roles
FROM users u
    LEFT JOIN user_roles ur ON u.id = ur.user_id
    LEFT JOIN roles r ON ur.role_id = r.id
GROUP BY u.id, u.user_name, u.full_name, u.email, u.status
ORDER BY u.user_name;

-- 5. Generate BCrypt password hash (chạy trong Java/Spring Boot)
/*
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String hashedPassword = encoder.encode(password);
        System.out.println("Hashed password: " + hashedPassword);
    }
}
*/
