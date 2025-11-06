# JWT Quick Start Guide

## 🚀 Bắt đầu nhanh trong 5 phút

### Bước 1: Generate Password Hash

```bash
# Windows
cd api
gradlew.bat run --main-class=ecommerce.api.util.PasswordHashGenerator

# Linux/Mac
cd api
./gradlew run --main-class=ecommerce.api.util.PasswordHashGenerator
```

Copy hash từ output, ví dụ: `$2a$10$ABC123...`

### Bước 2: Update Database

Mở file `test-data.sql`, tìm và thay thế:

```sql
-- Tìm dòng này
'$2a$10$qNGqZQwvZ7YxGGxJ0LY5WeN4X8Z5iH8dQPxN3tYGJQN8X8Z5iH8dQP'

-- Thay bằng hash vừa generate
'$2a$10$ABC123...'  -- Hash của bạn
```

Chạy SQL:

```bash
psql -h 52.77.101.54 -U ecommerce -d ecommerce_db -f test-data.sql
```

### Bước 3: Run Application

```bash
# Windows
cd api
gradlew.bat bootRun

# Linux/Mac
cd api
./gradlew bootRun
```

### Bước 4: Test Login

**Option 1: cURL**

```bash
curl -X POST http://localhost:8080/api/auth/login ^
  -H "Content-Type: application/json" ^
  -d "{\"username\":\"admin\",\"password\":\"password123\"}"
```

**Option 2: Postman**

1. Import `postman-collection.json`
2. Run "Login - Admin"
3. Token tự động lưu

**Option 3: Swagger UI**

1. Open: http://localhost:8080/swagger-ui.html
2. Test endpoint `/api/auth/login`

### Bước 5: Test Protected Endpoint

Copy `accessToken` từ login response, sau đó:

```bash
curl -X GET http://localhost:8080/api/demo/user ^
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

✅ **Hoàn tất!** JWT đã hoạt động.

---

## 📋 Test Users

| Username | Password    | Roles         |
| -------- | ----------- | ------------- |
| admin    | password123 | ADMIN, USER   |
| user1    | password123 | USER          |
| manager1 | password123 | MANAGER, USER |

## 🔗 Endpoints quan trọng

- **Login**: `POST /api/auth/login`
- **Refresh**: `POST /api/auth/refresh`
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/v3/api-docs

## 📖 Chi tiết hơn?

- Đọc `JWT_IMPLEMENTATION.md` - Implementation chi tiết
- Đọc `TESTING_GUIDE.md` - Hướng dẫn test đầy đủ
- Xem `JWT_SUMMARY.md` - Tổng quan toàn bộ

## ❗ Lỗi thường gặp

**"Bad credentials"**
→ Password trong DB chưa đúng hash. Chạy lại Bước 1 & 2.

**"User not found"**  
→ Chưa chạy `test-data.sql`. Chạy lại Bước 2.

**Connection refused**
→ Database chưa chạy hoặc config sai trong `application.properties`.

**Port 8080 đã sử dụng**
→ Thêm vào `application.properties`: `server.port=8081`
