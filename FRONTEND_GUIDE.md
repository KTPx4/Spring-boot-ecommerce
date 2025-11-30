# Frontend Developer Guide - E-Commerce API

## 🚀 Quick Start

### 1. Access Swagger UI
Once the backend is running, access the interactive API documentation at:
```
http://localhost:8080/swagger-ui/index.html
```

### 2. Authentication Flow
```
Register → Login → Get JWT Token → Use Token in Headers
```

### 3. First API Call Example
```javascript
// Login to get token
const response = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'your_username',
    password: 'your_password'
  })
});

const { accessToken } = await response.json();

// Use token for protected endpoints
const profile = await fetch('http://localhost:8080/api/v1/user/profile', {
  headers: {
    'Authorization': `Bearer ${accessToken}`
  }
});
```

---

## 📋 Complete API Documentation

**📄 Full API Documentation:** [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

This comprehensive guide includes:
- All endpoint specifications
- Request/response examples
- cURL commands
- JavaScript code examples
- Authentication guide
- Error handling
- Common use cases

---

## 🔑 Authentication System

### Token Types

| Token Type | Lifetime | Usage |
|------------|----------|-------|
| **Access Token** | 24 hours | Used in `Authorization` header for all protected endpoints |
| **Refresh Token** | Longer-lived | Used to obtain new access token without re-login |

### How to Use JWT Tokens

**Step 1: Get Tokens**
```javascript
POST /api/auth/login
{
  "username": "johndoe",
  "password": "SecurePass123!"
}

// Response:
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

**Step 2: Store Tokens Securely**
```javascript
// localStorage (simple but less secure)
localStorage.setItem('accessToken', accessToken);
localStorage.setItem('refreshToken', refreshToken);

// OR httpOnly cookie (more secure, requires backend support)
// OR sessionStorage (cleared when tab closes)
```

**Step 3: Use Access Token**
```javascript
fetch('http://localhost:8080/api/v1/user/profile', {
  headers: {
    'Authorization': `Bearer ${localStorage.getItem('accessToken')}`
  }
})
```

**Step 4: Refresh When Expired**
```javascript
// If you get 401 Unauthorized
POST /api/auth/refresh
{
  "refreshToken": "your_refresh_token"
}

// You'll get new tokens
```

---

## 🌐 API Endpoints Overview

### Public Endpoints (No Authentication Required)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/register` | POST | Register new user account |
| `/api/auth/login` | POST | Login and get JWT tokens |
| `/api/auth/refresh` | POST | Refresh access token |
| `/api/v1/product` | GET | Get all products |
| `/api/v1/product/{id}` | GET | Get product by ID |
| `/api/v1/brand` | GET | Get all brands |
| `/api/v1/brand/{id}` | GET | Get brand by ID |
| `/api/v1/category` | GET | Get all categories |
| `/api/v1/category/{id}` | GET | Get category by ID |

### Protected Endpoints (USER Role)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/auth/test` | GET | Test JWT authentication |
| `/api/v1/user/profile` | GET | Get current user profile |
| `/api/v1/user/profile` | PUT | Update current user profile |
| `/api/v1/user/{id}` | GET | Get user by ID |
| `/api/v1/images/upload` | POST | Upload single image |
| `/api/v1/images/upload-multiple` | POST | Upload multiple images |

### Admin Only Endpoints (ADMIN Role)

| Endpoint | Method | Description |
|----------|--------|-------------|
| `/api/v1/product` | POST | Create new product |
| `/api/v1/product/{id}` | PUT | Update product |
| `/api/v1/product/{id}` | DELETE | Delete product |
| `/api/v1/brand` | POST | Create new brand |
| `/api/v1/brand/{id}` | PUT | Update brand |
| `/api/v1/brand/{id}` | DELETE | Delete brand |
| `/api/v1/category` | POST | Create new category |
| `/api/v1/category/{id}` | PUT | Update category |
| `/api/v1/category/{id}` | DELETE | Delete category |
| `/api/v1/images/{path}` | DELETE | Delete image |

---

## 📦 Response Format

**All responses follow this structure:**

### Success Response
```json
{
  "status": "success",
  "message": "Operation completed successfully",
  "data": { /* actual response data */ }
}
```

### Error Response
```json
{
  "status": "error",
  "message": "Error description",
  "data": null
}
```

---

## 🎨 Image Upload Guide

### Upload Single Image

```javascript
const formData = new FormData();
formData.append('image', fileInput.files[0]);
formData.append('category', 'product'); // or 'user', 'brand', 'category'
formData.append('alt', 'Product image description');

const response = await fetch('http://localhost:8080/api/v1/images/upload', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${accessToken}`
  },
  body: formData
});

const result = await response.json();
console.log(result.data.url); // Use this URL for imgUrl fields
```

### Upload Response Includes

- **Full URL**: Original image URL
- **Thumbnails**: Small, medium, large versions
- **Metadata**: Original filename, size, dimensions, format

### Supported Image Formats
- JPG/JPEG
- PNG
- GIF
- WEBP

### Image Size Limits
- Maximum 10 files per upload (for multiple upload)
- Recommended max file size: 5MB per image

---

## 💡 Common Integration Patterns

### Pattern 1: React Authentication Hook

```javascript
// useAuth.js
import { useState, useEffect } from 'react';

export function useAuth() {
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(true);
  
  useEffect(() => {
    const token = localStorage.getItem('accessToken');
    if (token) {
      fetchProfile(token);
    } else {
      setLoading(false);
    }
  }, []);
  
  const fetchProfile = async (token) => {
    try {
      const response = await fetch('http://localhost:8080/api/v1/user/profile', {
        headers: { 'Authorization': `Bearer ${token}` }
      });
      
      if (response.ok) {
        const data = await response.json();
        setUser(data.data);
      } else {
        logout();
      }
    } catch (error) {
      logout();
    } finally {
      setLoading(false);
    }
  };
  
  const login = async (username, password) => {
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password })
    });
    
    if (response.ok) {
      const { accessToken, refreshToken } = await response.json();
      localStorage.setItem('accessToken', accessToken);
      localStorage.setItem('refreshToken', refreshToken);
      await fetchProfile(accessToken);
      return true;
    }
    return false;
  };
  
  const logout = () => {
    localStorage.removeItem('accessToken');
    localStorage.removeItem('refreshToken');
    setUser(null);
  };
  
  return { user, loading, login, logout };
}
```

### Pattern 2: Vue.js API Service

```javascript
// api.service.js
class ApiService {
  constructor(baseURL = 'http://localhost:8080') {
    this.baseURL = baseURL;
  }
  
  getAuthHeaders() {
    const token = localStorage.getItem('accessToken');
    return {
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    };
  }
  
  async get(endpoint) {
    const response = await fetch(`${this.baseURL}${endpoint}`, {
      headers: this.getAuthHeaders()
    });
    return this.handleResponse(response);
  }
  
  async post(endpoint, data) {
    const response = await fetch(`${this.baseURL}${endpoint}`, {
      method: 'POST',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data)
    });
    return this.handleResponse(response);
  }
  
  async put(endpoint, data) {
    const response = await fetch(`${this.baseURL}${endpoint}`, {
      method: 'PUT',
      headers: this.getAuthHeaders(),
      body: JSON.stringify(data)
    });
    return this.handleResponse(response);
  }
  
  async delete(endpoint) {
    const response = await fetch(`${this.baseURL}${endpoint}`, {
      method: 'DELETE',
      headers: this.getAuthHeaders()
    });
    return this.handleResponse(response);
  }
  
  async handleResponse(response) {
    if (response.status === 401) {
      // Token expired, try refresh
      const refreshed = await this.refreshToken();
      if (!refreshed) {
        // Redirect to login
        window.location.href = '/login';
        throw new Error('Session expired');
      }
    }
    
    const data = await response.json();
    if (!response.ok) {
      throw new Error(data.message || 'Request failed');
    }
    return data.data;
  }
  
  async refreshToken() {
    const refreshToken = localStorage.getItem('refreshToken');
    if (!refreshToken) return false;
    
    try {
      const response = await fetch(`${this.baseURL}/api/auth/refresh`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ refreshToken })
      });
      
      if (response.ok) {
        const { accessToken, refreshToken: newRefreshToken } = await response.json();
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', newRefreshToken);
        return true;
      }
    } catch (error) {
      console.error('Token refresh failed:', error);
    }
    return false;
  }
}

export default new ApiService();

// Usage:
// import api from './api.service';
// const products = await api.get('/api/v1/product');
// const product = await api.post('/api/v1/product', productData);
```

### Pattern 3: Angular HTTP Interceptor

```typescript
// auth.interceptor.ts
import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError, BehaviorSubject } from 'rxjs';
import { catchError, switchMap, filter, take } from 'rxjs/operators';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  private refreshTokenSubject: BehaviorSubject<any> = new BehaviorSubject<any>(null);

  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('accessToken');
    
    if (token) {
      req = this.addToken(req, token);
    }
    
    return next.handle(req).pipe(
      catchError(error => {
        if (error instanceof HttpErrorResponse && error.status === 401) {
          return this.handle401Error(req, next);
        }
        return throwError(() => error);
      })
    );
  }
  
  private addToken(request: HttpRequest<any>, token: string) {
    return request.clone({
      setHeaders: {
        'Authorization': `Bearer ${token}`
      }
    });
  }
  
  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;
      this.refreshTokenSubject.next(null);
      
      const refreshToken = localStorage.getItem('refreshToken');
      
      if (refreshToken) {
        return this.refreshToken(refreshToken).pipe(
          switchMap((token: any) => {
            this.isRefreshing = false;
            this.refreshTokenSubject.next(token.accessToken);
            return next.handle(this.addToken(request, token.accessToken));
          }),
          catchError((err) => {
            this.isRefreshing = false;
            // Redirect to login
            window.location.href = '/login';
            return throwError(() => err);
          })
        );
      }
    }
    
    return this.refreshTokenSubject.pipe(
      filter(token => token != null),
      take(1),
      switchMap(token => next.handle(this.addToken(request, token)))
    );
  }
  
  private refreshToken(token: string): Observable<any> {
    // Implement refresh token API call
    return this.http.post('http://localhost:8080/api/auth/refresh', { refreshToken: token });
  }
}
```

---

## 🔍 Testing with Swagger UI

1. **Start Backend**: Run the Spring Boot application
2. **Open Swagger**: Navigate to `http://localhost:8080/swagger-ui/index.html`
3. **Authorize**:
   - Click the "Authorize" button (🔒) at the top right
   - Login via `/api/auth/login` endpoint first
   - Copy the `accessToken` from response
   - Paste in the "Value" field as: `Bearer <your_token>`
   - Click "Authorize" and "Close"
4. **Test Endpoints**: All your requests will now include the JWT token

---

## ⚠️ Error Codes Reference

| HTTP Code | Meaning | Common Causes |
|-----------|---------|---------------|
| **200** | Success | Request completed successfully |
| **400** | Bad Request | Invalid input data, validation failed |
| **401** | Unauthorized | Missing/invalid JWT token, token expired |
| **403** | Forbidden | Valid token but insufficient permissions (not ADMIN) |
| **404** | Not Found | Resource (product/user/etc) doesn't exist |
| **500** | Server Error | Backend error, CDN unavailable |

---

## 📱 Mobile App Integration

### iOS (Swift)

```swift
struct APIService {
    static let baseURL = "http://localhost:8080"
    
    static func login(username: String, password: String) async throws -> LoginResponse {
        let url = URL(string: "\(baseURL)/api/auth/login")!
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")
        
        let body = ["username": username, "password": password]
        request.httpBody = try JSONEncoder().encode(body)
        
        let (data, _) = try await URLSession.shared.data(for: request)
        let response = try JSONDecoder().decode(LoginResponse.self, from: data)
        
        // Store tokens
        UserDefaults.standard.set(response.accessToken, forKey: "accessToken")
        UserDefaults.standard.set(response.refreshToken, forKey: "refreshToken")
        
        return response
    }
    
    static func getProfile() async throws -> UserProfile {
        let url = URL(string: "\(baseURL)/api/v1/user/profile")!
        var request = URLRequest(url: url)
        
        if let token = UserDefaults.standard.string(forKey: "accessToken") {
            request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        }
        
        let (data, _) = try await URLSession.shared.data(for: request)
        let response = try JSONDecoder().decode(APIResponse<UserProfile>.self, from: data)
        return response.data
    }
}
```

### Android (Kotlin)

```kotlin
class ApiService {
    private val baseURL = "http://localhost:8080"
    private val client = OkHttpClient()
    private val json = Json { ignoreUnknownKeys = true }
    
    suspend fun login(username: String, password: String): LoginResponse {
        val requestBody = """
            {"username":"$username","password":"$password"}
        """.trimIndent()
        
        val request = Request.Builder()
            .url("$baseURL/api/auth/login")
            .post(requestBody.toRequestBody("application/json".toMediaType()))
            .build()
        
        val response = client.newCall(request).execute()
        val loginResponse = json.decodeFromString<LoginResponse>(response.body!!.string())
        
        // Store tokens in SharedPreferences
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        prefs.edit()
            .putString("accessToken", loginResponse.accessToken)
            .putString("refreshToken", loginResponse.refreshToken)
            .apply()
        
        return loginResponse
    }
    
    suspend fun getProfile(): UserProfile {
        val token = getAccessToken()
        
        val request = Request.Builder()
            .url("$baseURL/api/v1/user/profile")
            .header("Authorization", "Bearer $token")
            .build()
        
        val response = client.newCall(request).execute()
        val apiResponse = json.decodeFromString<ApiResponse<UserProfile>>(response.body!!.string())
        return apiResponse.data
    }
    
    private fun getAccessToken(): String {
        val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        return prefs.getString("accessToken", "") ?: ""
    }
}
```

---

## 🛠️ Development Tips

### 1. Environment Variables

Create a `.env` file or configuration for different environments:

```javascript
// config.js
const config = {
  development: {
    apiUrl: 'http://localhost:8080'
  },
  production: {
    apiUrl: 'http://52.77.101.54:8080'
  }
};

export default config[process.env.NODE_ENV || 'development'];
```

### 2. CORS Configuration

If you encounter CORS issues during development, the backend should have CORS enabled for:
- `http://localhost:3000` (React default)
- `http://localhost:4200` (Angular default)
- `http://localhost:8081` (Vue default)

### 3. Request Logging

Enable request/response logging during development:

```javascript
const originalFetch = window.fetch;
window.fetch = function(...args) {
  console.log('API Request:', args);
  return originalFetch.apply(this, args)
    .then(response => {
      console.log('API Response:', response);
      return response;
    });
};
```

---

## 📚 Resources

- **Swagger UI**: `http://localhost:8080/swagger-ui/index.html`
- **API Docs JSON**: `http://localhost:8080/v3/api-docs`
- **Full Documentation**: [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)

---

## 🤝 Support

If you encounter issues or need clarification:
1. Check the Swagger UI for live API documentation
2. Review the full [API_DOCUMENTATION.md](./API_DOCUMENTATION.md)
3. Contact the backend development team

---

**Happy Coding! 🎉**
