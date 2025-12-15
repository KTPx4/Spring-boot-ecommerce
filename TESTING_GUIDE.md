# Hướng dẫn Test JWT Authentication

## Bước 1: Generate Password Hash

Chạy class `PasswordHashGenerator` để tạo BCrypt hash cho password "password123":

```bash
cd api
./gradlew run --main-class=ecommerce.api.util.PasswordHashGenerator
```

Hoặc run trực tiếp trong IDE (IntelliJ IDEA/Eclipse).

Output sẽ cho bạn hashed password, ví dụ:

```
Hashed password: $2a$10$XYZ123...
```

## Bước 2: Cập nhật Database

1. Copy hashed password từ bước 1
2. Mở file `test-data.sql`
3. Thay thế tất cả `$2a$10$qNGqZQwvZ7YxGGxJ0LY5WeN4X8Z5iH8dQPxN3tYGJQN8X8Z5iH8dQP` bằng hash vừa generate
4. Chạy SQL script:

```sql
-- Kết nối đến database của bạn
psql -h 52.77.101.54 -U ecommerce -d ecommerce_db

-- Chạy script
\i test-data.sql
```

Hoặc sử dụng tool như pgAdmin, DBeaver.

## Bước 3: Build Project

```bash
# Từ thư mục root
./gradlew clean build

# Hoặc chỉ build module api
cd api
./gradlew clean build
```

## Bước 4: Run Application

```bash
cd api
./gradlew bootRun
```

Application sẽ chạy tại: `http://localhost:8080`

## Bước 5: Test với Postman

### Import Collection

1. Mở Postman
2. Click "Import"
3. Chọn file `postman-collection.json`
4. Collection "E-Commerce JWT API" sẽ được thêm vào

### Test Login

1. Mở request "Authentication > Login - Admin"
2. Click "Send"
3. Nếu thành công, access token và refresh token sẽ tự động được lưu vào variables
4. Response:

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400000
}
```

### Test Protected Endpoints

1. **User Endpoint** (cần USER hoặc ADMIN role):

   - Request: "Demo Endpoints > User Endpoint"
   - Authorization tự động sử dụng {{accessToken}}
   - ✅ Admin và User đều có thể truy cập

2. **Admin Endpoint** (chỉ ADMIN):

   - Request: "Demo Endpoints > Admin Endpoint"
   - ✅ Admin có thể truy cập
   - ❌ User thường sẽ nhận 403 Forbidden

3. **Public Endpoint** (không cần authentication):
   - Request: "Demo Endpoints > Public Endpoint"
   - Không cần token

## Bước 6: Test với Swagger UI

1. Mở browser: `http://localhost:8080/swagger-ui.html`
2. Click nút "Authorize" (🔓) ở góc trên
3. Nhập: `Bearer {accessToken}` (copy từ login response)
4. Click "Authorize"
5. Giờ có thể test các API trực tiếp trên Swagger

## Bước 7: Test với cURL

### Login

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }'
```

### Lưu token vào biến

```bash
# Lưu response vào file
response=$(curl -s -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "admin",
    "password": "password123"
  }')

# Extract access token (cần jq)
token=$(echo $response | jq -r '.accessToken')

# Hoặc thủ công copy token từ response
```

### Test protected endpoint

```bash
curl -X GET http://localhost:8080/api/demo/admin \
  -H "Authorization: Bearer YOUR_TOKEN_HERE"
```

## Test Cases

### ✅ Test Case 1: Login thành công với Admin

- Username: `admin`
- Password: `password123`
- Expected: 200 OK, trả về tokens

### ✅ Test Case 2: Login thành công với User

- Username: `user1`
- Password: `password123`
- Expected: 200 OK, trả về tokens

### ❌ Test Case 3: Login với password sai

- Username: `admin`
- Password: `wrongpassword`
- Expected: 401 Unauthorized

### ❌ Test Case 4: Login với user không tồn tại

- Username: `notexist`
- Password: `password123`
- Expected: 404 Not Found

### ❌ Test Case 5: Login với inactive user

- Username: `inactive_user`
- Password: `password123`
- Expected: 404 Not Found (user inactive)

### ✅ Test Case 6: Access user endpoint với Admin

- Endpoint: `/api/demo/user`
- Token: Admin token
- Expected: 200 OK

### ✅ Test Case 7: Access user endpoint với User

- Endpoint: `/api/demo/user`
- Token: User token
- Expected: 200 OK

### ✅ Test Case 8: Access admin endpoint với Admin

- Endpoint: `/api/demo/admin`
- Token: Admin token
- Expected: 200 OK

### ❌ Test Case 9: Access admin endpoint với User

- Endpoint: `/api/demo/admin`
- Token: User token
- Expected: 403 Forbidden

### ❌ Test Case 10: Access protected endpoint không có token

- Endpoint: `/api/demo/user`
- Token: Không có
- Expected: 401 Unauthorized

### ❌ Test Case 11: Access protected endpoint với invalid token

- Endpoint: `/api/demo/user`
- Token: `Bearer invalid_token_string`
- Expected: 401 Unauthorized

### ✅ Test Case 12: Refresh token

- Endpoint: `/api/auth/refresh`
- Body: `{"refreshToken": "valid_refresh_token"}`
- Expected: 200 OK, trả về tokens mới

## Troubleshooting

### Lỗi: "Bad credentials"

- **Nguyên nhân**: Password sai hoặc password trong DB chưa được hash
- **Giải pháp**:
  1. Check password đúng chưa
  2. Chạy lại `PasswordHashGenerator` và update DB

### Lỗi: "User not found"

- **Nguyên nhân**: Username không tồn tại trong DB
- **Giải pháp**: Chạy lại `test-data.sql`

### Lỗi: "Invalid JWT signature"

- **Nguyên nhân**: JWT secret không khớp
- **Giải pháp**: Check `jwt.secret` trong application.properties

### Lỗi: "Expired JWT token"

- **Nguyên nhân**: Token đã hết hạn (> 24h)
- **Giải pháp**: Login lại hoặc dùng refresh token

### Lỗi: 403 Forbidden

- **Nguyên nhân**: User không có quyền truy cập endpoint
- **Giải pháp**:
  1. Check role của user trong DB
  2. Check annotation `// @PreAuthorize` của endpoint

## Logs

Check logs để debug:

```bash
# API logs
tail -f api/logs/app.log

# Console logs
# Xem trong terminal khi chạy ./gradlew bootRun
```

## Database Queries hữu ích

### Xem users và roles

```sql
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
```

### Update password của user

```sql
UPDATE users
SET pass_hash = '$2a$10$YOUR_NEW_HASH_HERE'
WHERE user_name = 'admin';
```

### Active/Inactive user

```sql
-- Active
UPDATE users SET status = 1 WHERE user_name = 'user1';

-- Inactive
UPDATE users SET status = 0 WHERE user_name = 'user1';
```

## Next Steps

Sau khi test thành công, bạn có thể:

1. **Tùy chỉnh JWT expiration time** trong `application.properties`
2. **Thêm roles mới** và endpoints tương ứng
3. **Implement logout** với token blacklist
4. **Thêm refresh token rotation** để tăng bảo mật
5. **Implement password reset** functionality
6. **Thêm rate limiting** để chống brute force
7. **Setup CORS** nếu có frontend riêng
8. **Migrate sang HTTPS** trong production
