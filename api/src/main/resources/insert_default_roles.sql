-- Insert default roles if not exists
INSERT INTO roles
    (id, name, description, created_at, updated_at)
SELECT 1, 'USER', 'Default user role', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1
FROM roles
WHERE name = 'USER');

INSERT INTO roles
    (id, name, description, created_at, updated_at)
SELECT 2, 'ADMIN', 'Administrator role', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1
FROM roles
WHERE name = 'ADMIN');

INSERT INTO roles
    (id, name, description, created_at, updated_at)
SELECT 3, 'MODERATOR', 'Moderator role', NOW(), NOW()
WHERE NOT EXISTS (SELECT 1
FROM roles
WHERE name = 'MODERATOR');

-- Insert default admin user if not exists
-- Password: admin (BCrypt hashed: $2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy)
INSERT INTO users
    (id, user_name, pass_hash, full_name, email, status, created_at, updated_at)
SELECT 1, 'admin', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'System Administrator', 'admin@example.com', 1, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1
FROM users
WHERE user_name = 'admin');

-- Assign ADMIN role to admin user
INSERT INTO user_roles
    (id, user_id, role_id, created_at, updated_at)
SELECT 1, 1, 2, NOW(), NOW()
WHERE NOT EXISTS (SELECT 1
FROM user_roles
WHERE user_id = 1 AND role_id = 2);
