# JWT Architecture Diagram

## Clean Architecture Layers

```
┌─────────────────────────────────────────────────────────────────┐
│                         API LAYER                                │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Controllers                                                 │ │
│  │  • AuthenticationController (login, refresh)               │ │
│  │  • DemoController (test endpoints)                         │ │
│  └────────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Security Configuration                                      │ │
│  │  • SecurityConfig (Spring Security setup)                  │ │
│  │  • JwtAuthenticationFilter (validate JWT)                  │ │
│  │  • OpenApiConfig (Swagger setup)                           │ │
│  └────────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ DTOs & Exception Handling                                  │ │
│  │  • LoginRequest, LoginResponse                             │ │
│  │  • GlobalExceptionHandler                                  │ │
│  └────────────────────────────────────────────────────────────┘ │
└──────────────────────┬──────────────────────────────────────────┘
                       │ depends on
                       ▼
┌─────────────────────────────────────────────────────────────────┐
│                        INFRA LAYER                               │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ JWT Implementation                                          │ │
│  │  • JwtTokenProviderImpl (create & validate tokens)         │ │
│  └────────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Security Services                                           │ │
│  │  • CustomUserDetailsService (load user)                    │ │
│  │  • AuthenticationServiceImpl (handle login)                │ │
│  │  • UserRepositoryImpl (access database)                    │ │
│  └────────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ JPA Repositories                                            │ │
│  │  • UserJpaRepository                                        │ │
│  │  • UserRoleJpaRepository                                    │ │
│  └────────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Entities (JPA)                                              │ │
│  │  • User, Role, UserRole                                     │ │
│  └────────────────────────────────────────────────────────────┘ │
└──────────────────────┬──────────────────────────────────────────┘
                       │ depends on & implements
                       ▼
┌─────────────────────────────────────────────────────────────────┐
│                        CORE LAYER                                │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Domain Models                                               │ │
│  │  • JwtToken                                                 │ │
│  │  • AuthenticationRequest                                    │ │
│  │  • UserPrincipal                                            │ │
│  └────────────────────────────────────────────────────────────┘ │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │ Interfaces                                                  │ │
│  │  • JwtTokenProvider                                         │ │
│  │  • UserRepository                                           │ │
│  │  • AuthenticationService                                    │ │
│  └────────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────────┘
```

## Authentication Flow

```
┌────────┐                                                    ┌──────────┐
│ Client │                                                    │ Database │
└───┬────┘                                                    └────┬─────┘
    │                                                              │
    │ 1. POST /api/auth/login                                     │
    │    {username, password}                                      │
    ├──────────────────────────────────────────►                  │
    │         AuthenticationController                             │
    │                    │                                         │
    │                    ▼                                         │
    │         AuthenticationService                                │
    │                    │                                         │
    │                    ▼                                         │
    │         AuthenticationManager                                │
    │         (validate credentials)                               │
    │                    │                                         │
    │                    ▼                                         │
    │         CustomUserDetailsService                             │
    │                    │                                         │
    │                    ├────────────────────────────────────────►│
    │                    │  2. Load user from DB                   │
    │                    │     (with roles)                        │
    │                    │◄────────────────────────────────────────┤
    │                    │  3. Return user + roles                 │
    │                    ▼                                         │
    │         BCryptPasswordEncoder                                │
    │         (verify password hash)                               │
    │                    │                                         │
    │                    ▼                                         │
    │         JwtTokenProvider                                     │
    │         (generate access + refresh tokens)                   │
    │                    │                                         │
    │ 4. Return JwtToken │                                         │
    │◄───────────────────┤                                         │
    │   {accessToken,    │                                         │
    │    refreshToken,   │                                         │
    │    expiresIn}      │                                         │
    │                    │                                         │
    │ 5. Store tokens    │                                         │
    │                    │                                         │
    │ 6. GET /api/demo/user                                        │
    │    Authorization: Bearer {token}                             │
    ├──────────────────────────────────────────►                  │
    │         JwtAuthenticationFilter                              │
    │                    │                                         │
    │                    ▼                                         │
    │         JwtTokenProvider                                     │
    │         (validate token & extract username)                  │
    │                    │                                         │
    │                    ▼                                         │
    │         CustomUserDetailsService                             │
    │         (load user details)                                  │
    │                    │                                         │
    │                    ▼                                         │
    │         Spring Security Context                              │
    │         (set Authentication)                                 │
    │                    │                                         │
    │                    ▼                                         │
    │         DemoController                                       │
    │         (check @PreAuthorize)                                │
    │                    │                                         │
    │ 7. Return response │                                         │
    │◄───────────────────┤                                         │
    │                                                              │
    ▼                                                              ▼
```

## Database Schema

```
┌────────────────────┐
│       users        │
├────────────────────┤
│ id (PK)            │
│ user_name (UNIQUE) │
│ pass_hash          │
│ full_name          │
│ email (UNIQUE)     │
│ phone              │
│ status             │
│ created_at         │
│ updated_at         │
└─────────┬──────────┘
          │
          │ 1
          │
          │ *
┌─────────▼──────────┐
│    user_roles      │
├────────────────────┤
│ id (PK)            │
│ user_id (FK)       │
│ role_id (FK)       │
│ created_at         │
│ updated_at         │
└─────────┬──────────┘
          │
          │ *
          │
          │ 1
┌─────────▼──────────┐
│       roles        │
├────────────────────┤
│ id (PK)            │
│ name (UNIQUE)      │
│ description        │
│ created_at         │
│ updated_at         │
└────────────────────┘
```

## JWT Token Structure

```
Header
{
  "alg": "HS256",
  "typ": "JWT"
}

Payload
{
  "sub": "admin",              // username
  "userId": 1,
  "email": "admin@example.com",
  "fullName": "Admin User",
  "roles": ["ADMIN", "USER"],
  "iat": 1699200000,           // issued at
  "exp": 1699286400            // expires at
}

Signature
HMACSHA256(
  base64UrlEncode(header) + "." +
  base64UrlEncode(payload),
  secret
)

Final Token: xxxxx.yyyyy.zzzzz
```

## Request/Response Flow

### Login Request

```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "admin",
  "password": "password123"
}
```

### Login Response

```json
{
  "accessToken": "eyJhbGc...",
  "refreshToken": "eyJhbGc...",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

### Protected Request

```http
GET /api/demo/admin
Authorization: Bearer eyJhbGc...
```

### Success Response

```json
{
  "message": "This is admin endpoint",
  "username": "admin",
  "authorities": [
    {
      "authority": "ROLE_ADMIN"
    },
    {
      "authority": "ROLE_USER"
    }
  ]
}
```

### Error Response

```json
{
  "timestamp": "2025-11-06T10:30:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Invalid username or password",
  "path": "/api/auth/login"
}
```

## Component Dependencies

```
SecurityConfig
    ├── depends on: JwtAuthenticationFilter
    ├── depends on: CustomUserDetailsService
    └── provides: AuthenticationManager, PasswordEncoder

JwtAuthenticationFilter
    ├── depends on: JwtTokenProvider
    ├── depends on: CustomUserDetailsService
    └── validates: every request

AuthenticationServiceImpl
    ├── depends on: AuthenticationManager
    ├── depends on: JwtTokenProvider
    ├── depends on: CustomUserDetailsService
    └── handles: login & refresh

CustomUserDetailsService
    ├── depends on: UserRepository
    └── loads: user details for authentication

JwtTokenProviderImpl
    ├── implements: JwtTokenProvider
    └── uses: JJWT library

UserRepositoryImpl
    ├── implements: UserRepository
    ├── depends on: UserJpaRepository
    ├── depends on: UserRoleJpaRepository
    └── maps: Entity to Domain
```

## Security Annotations

```java
// Class level
@PreAuthorize("hasRole('ADMIN')")
public class AdminController { ... }

// Method level
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<?> deleteUser() { ... }

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public ResponseEntity<?> getProfile() { ... }

@PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
public ResponseEntity<?> createProduct() { ... }

// Multiple conditions
@PreAuthorize("hasRole('ADMIN') and hasAuthority('WRITE_PRIVILEGE')")
public ResponseEntity<?> sensitiveOperation() { ... }

// Check user ownership
@PreAuthorize("hasRole('USER') and #userId == authentication.principal.userId")
public ResponseEntity<?> getUserData(@PathVariable Long userId) { ... }
```
