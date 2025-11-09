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
