# JWT Authentication Implementation Guide

## Tổng quan

Dự án đã được tích hợp JWT (JSON Web Token) để xác thực và phân quyền theo kiến trúc Clean Architecture.

## Cấu trúc triển khai

### 1. Core Layer (`core/`)

- **Domain Models**: `JwtToken`, `AuthenticationRequest`, `UserPrincipal`
- **Interfaces**: `JwtTokenProvider`, `UserRepository`, `AuthenticationService`

### 2. Infra Layer (`infra/`)

- **JWT Implementation**: `JwtTokenProviderImpl` - Tạo và validate JWT tokens
- **User Service**: `CustomUserDetailsService` - Load user từ database
- **Repository**: `UserRepositoryImpl` - Implement UserRepository interface
- **JPA Repositories**: `UserJpaRepository`, `UserRoleJpaRepository`
- **Authentication Service**: `AuthenticationServiceImpl` - Xử lý login và refresh token

### 3. API Layer (`api/`)

- **Security Config**: `SecurityConfig` - Cấu hình Spring Security
- **JWT Filter**: `JwtAuthenticationFilter` - Filter để validate JWT trong mỗi request
- **Controller**: `AuthenticationController` - API endpoints cho authentication
- **DTOs**: `LoginRequest`, `LoginResponse`, `RefreshTokenRequest`
- **Demo Controller**: `DemoController` - Test authorization với các roles

## Cấu hình

### application.properties

```properties
# JWT Configuration
jwt.secret=5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437
jwt.expiration=86400000          # 24 hours
jwt.refresh-expiration=604800000  # 7 days
```

### Dependencies (build.gradle)

```groovy
// JWT dependencies
implementation 'io.jsonwebtoken:jjwt-api:0.12.5'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.12.5'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.12.5'
```

## API Endpoints

### Authentication Endpoints

#### 1. Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user123",
  "password": "password123"
}
```

**Response:**

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

#### 2. Refresh Token

```http
POST /api/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### 3. Test Authentication

```http
GET /api/auth/test
Authorization: Bearer {accessToken}
```

### Demo Endpoints (Protected)

#### User Endpoint (Requires USER or ADMIN role)

```http
GET /api/demo/user
Authorization: Bearer {accessToken}
```

#### Admin Endpoint (Requires ADMIN role only)

```http
GET /api/demo/admin
Authorization: Bearer {accessToken}
```

#### Public Endpoint (No authentication required)

```http
GET /api/demo/public
```

## Sử dụng JWT trong requests

### Thêm JWT vào header

```http
Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
```

### Swagger UI

1. Mở Swagger UI: `http://localhost:8080/swagger-ui.html`
2. Click nút "Authorize" ở góc trên bên phải
3. Nhập token theo format: `Bearer {accessToken}`
4. Click "Authorize"

## Flow xác thực

```
1. Client gửi username/password → POST /api/auth/login
2. Server validate credentials
3. Server tạo JWT tokens (access + refresh)
4. Client lưu tokens
5. Client gửi request với access token trong header
6. JWT Filter validate token
7. Spring Security set authentication context
8. Request được xử lý với quyền tương ứng
```

## Phân quyền với Annotations

### @PreAuthorize

```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> adminOnly() { ... }

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public ResponseEntity<?> userOrAdmin() { ... }

@PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
public ResponseEntity<?> writeAccess() { ... }
```

## Database Schema

### Tables

- `users` - Lưu thông tin user
- `roles` - Lưu các role (ADMIN, USER, etc.)
- `user_roles` - Bảng nhiều-nhiều giữa users và roles

### Quan hệ

```
User 1---* UserRole *---1 Role
```

## Security Configuration

### Public endpoints (không cần authentication)

- `/api/auth/**` - Authentication endpoints
- `/api/public/**` - Public APIs
- `/swagger-ui/**` - Swagger UI
- `/v3/api-docs/**` - API documentation

### Protected endpoints

- Tất cả các endpoints khác yêu cầu JWT token hợp lệ

## Testing

### 1. Tạo user test trong database

```sql
-- Tạo user
INSERT INTO users (user_name, pass_hash, full_name, email, status, created_at, updated_at)
VALUES ('admin', '$2a$10$...', 'Admin User', 'admin@example.com', 1, NOW(), NOW());

-- Tạo role
INSERT INTO roles (name, description, created_at, updated_at)
VALUES ('ADMIN', 'Administrator role', NOW(), NOW());

-- Gán role cho user
INSERT INTO user_roles (user_id, role_id, created_at, updated_at)
VALUES (1, 1, NOW(), NOW());
```

**Note**: Password cần được hash bằng BCrypt. Ví dụ: `password123` → `$2a$10$...`

### 2. Test flow

```bash
# 1. Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password123"}'

# 2. Sử dụng access token
curl -X GET http://localhost:8080/api/demo/admin \
  -H "Authorization: Bearer {accessToken}"

# 3. Refresh token khi hết hạn
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{"refreshToken":"{refreshToken}"}'
```

## Bảo mật

### Best Practices

1. **JWT Secret**: Thay đổi `jwt.secret` trong production (min 256 bits)
2. **HTTPS**: Luôn sử dụng HTTPS trong production
3. **Token Storage**:
   - Access token: Memory (sessionStorage/localStorage)
   - Refresh token: HttpOnly Cookie (khuyến nghị)
4. **Token Expiration**:
   - Access token: Ngắn (15-30 phút)
   - Refresh token: Dài hơn (7-30 ngày)
5. **Password**: Luôn hash với BCrypt (strength ≥ 10)

### Generate JWT Secret

```bash
# Generate random 256-bit key
openssl rand -base64 32
```

## Troubleshooting

### Lỗi thường gặp

#### 1. "Invalid JWT signature"

- Kiểm tra `jwt.secret` đúng
- Đảm bảo token không bị modify

#### 2. "Expired JWT token"

- Token đã hết hạn, sử dụng refresh token để lấy token mới

#### 3. "User not found"

- User không tồn tại trong database
- Kiểm tra username chính xác

#### 4. "Bad credentials"

- Password sai
- Password trong DB chưa được hash

#### 5. "Access Denied" / 403

- User không có quyền truy cập endpoint
- Kiểm tra roles trong database

## Mở rộng

### Thêm roles mới

1. Thêm role vào bảng `roles`
2. Gán role cho user qua `user_roles`
3. Sử dụng `@PreAuthorize("hasRole('NEW_ROLE')")`

### Custom claims trong JWT

Chỉnh sửa `JwtTokenProviderImpl.generateToken()`:

```java
claims.put("customField", value);
```

### Logout

Implement token blacklist:

1. Lưu revoked tokens vào Redis/Database
2. Check blacklist trong JwtAuthenticationFilter

## Tài liệu tham khảo

- [JWT.io](https://jwt.io/)
- [Spring Security Documentation](https://docs.spring.io/spring-security/reference/)
- [JJWT GitHub](https://github.com/jwtk/jjwt)
