# E-Commerce API Documentation

## Table of Contents
- [Overview](#overview)
- [Base URL](#base-url)
- [Authentication](#authentication)
- [Response Format](#response-format)
- [Error Handling](#error-handling)
- [API Endpoints](#api-endpoints)
  - [Authentication](#authentication-endpoints)
  - [User Profile](#user-profile-endpoints)
  - [Products](#products-endpoints)
  - [Brands](#brands-endpoints)
  - [Categories](#categories-endpoints)
  - [Image Upload](#image-upload-endpoints)

---

## Overview

This is a RESTful API for an e-commerce platform built with Spring Boot following Clean Architecture principles. The API uses JWT (JSON Web Tokens) for authentication and authorization.

## Base URL

```
http://localhost:8080
```

## Authentication

### JWT Authentication Flow

1. **Register** a new account via `/api/auth/register`
2. **Login** with credentials via `/api/auth/login` to receive JWT tokens
3. **Use** the access token in the `Authorization` header for protected endpoints
4. **Refresh** the token when it expires using `/api/auth/refresh`

### Using JWT Token

For all protected endpoints, include the JWT token in the Authorization header:

```
Authorization: Bearer <your_access_token>
```

### Token Expiration

- **Access Token**: Expires in 24 hours
- **Refresh Token**: Use to get a new access token without logging in again

---

## Response Format

All API responses follow this standard format:

### Success Response
```json
{
  "status": "success",
  "message": "Operation completed successfully",
  "data": { ... }
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

## Error Handling

### Common HTTP Status Codes

| Status Code | Description |
|-------------|-------------|
| 200 | Success |
| 400 | Bad Request - Invalid input |
| 401 | Unauthorized - Missing or invalid JWT token |
| 403 | Forbidden - Insufficient permissions (e.g., requires ADMIN role) |
| 404 | Not Found - Resource doesn't exist |
| 500 | Internal Server Error |

---

## API Endpoints

### Authentication Endpoints

#### 1. Register New User

**POST** `/api/auth/register`

**Public Endpoint** - No authentication required

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "SecurePass123!",
  "email": "john@example.com",
  "fullName": "John Doe",
  "phone": "+1234567890",
  "birthDay": "1990-01-15",
  "gender": "Male"
}
```

**Response:**
```json
{
  "status": "success",
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "phone": "+1234567890",
    "createdAt": "2024-01-15T10:30:00"
  }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"johndoe\",\"password\":\"SecurePass123!\",\"email\":\"john@example.com\",\"fullName\":\"John Doe\",\"phone\":\"+1234567890\",\"birthDay\":\"1990-01-15\",\"gender\":\"Male\"}"
```

---

#### 2. Login

**POST** `/api/auth/login`

**Public Endpoint** - No authentication required

**Request Body:**
```json
{
  "username": "johndoe",
  "password": "SecurePass123!"
}
```

**Response:**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d "{\"username\":\"johndoe\",\"password\":\"SecurePass123!\"}"
```

---

#### 3. Refresh Token

**POST** `/api/auth/refresh`

**Public Endpoint** - No authentication required (uses refresh token)

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Response:** (Same as login response)

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d "{\"refreshToken\":\"your_refresh_token_here\"}"
```

---

#### 4. Test Authentication

**GET** `/api/auth/test`

**Protected Endpoint** - Requires JWT token

**Headers:**
```
Authorization: Bearer <your_access_token>
```

**Response:**
```
Authentication successful!
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/auth/test \
  -H "Authorization: Bearer your_access_token_here"
```

---

### User Profile Endpoints

#### 1. Get Current User Profile

**GET** `/api/v1/user/profile`

**Protected Endpoint** - Requires JWT token

**Headers:**
```
Authorization: Bearer <your_access_token>
```

**Response:**
```json
{
  "status": "success",
  "message": "Get profile success",
  "data": {
    "id": 1,
    "username": "johndoe",
    "email": "john@example.com",
    "fullName": "John Doe",
    "phone": "+1234567890",
    "birthDay": "1990-01-15",
    "imgUrl": "https://cdn.example.com/users/profile.jpg",
    "gender": "Male",
    "status": true,
    "roles": ["USER"],
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-20T14:00:00"
  }
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/v1/user/profile \
  -H "Authorization: Bearer your_access_token_here"
```

---

#### 2. Update Current User Profile

**PUT** `/api/v1/user/profile`

**Protected Endpoint** - Requires JWT token

**Headers:**
```
Authorization: Bearer <your_access_token>
```

**Request Body:**
```json
{
  "email": "newemail@example.com",
  "fullName": "John Updated Doe",
  "phone": "+1987654321",
  "birthDay": "1990-01-15",
  "gender": "Male",
  "imgUrl": "https://cdn.example.com/users/new-profile.jpg",
  "currentPassword": "OldPassword123!",
  "newPassword": "NewSecurePass456!"
}
```

**Note:** 
- All fields are optional - only send fields you want to update
- To change password, you MUST provide both `currentPassword` and `newPassword`
- Email must be unique (not used by another user)

**Response:** (Same as Get Profile response)

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/v1/user/profile \
  -H "Authorization: Bearer your_access_token_here" \
  -H "Content-Type: application/json" \
  -d "{\"fullName\":\"John Updated Doe\",\"phone\":\"+1987654321\"}"
```

---

#### 3. Get User by ID

**GET** `/api/v1/user/{id}`

**Protected Endpoint** - Requires JWT token

**Path Parameters:**
- `id` (Long) - User ID

**Response:** (Same as Get Profile response)

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/v1/user/1 \
  -H "Authorization: Bearer your_access_token_here"
```

---

### Products Endpoints

#### 1. Get All Products

**GET** `/api/v1/product`

**Public Endpoint** - No authentication required

**Response:**
```json
{
  "status": "success",
  "message": "Get all products success",
  "data": [
    {
      "id": 1,
      "name": "iPhone 15 Pro",
      "description": "Latest iPhone with A17 chip",
      "price": 999.99,
      "stock": 50,
      "imgUrl": "https://cdn.example.com/products/iphone15.jpg",
      "categoryId": 1,
      "brandId": 1,
      "createdAt": "2024-01-10T09:00:00",
      "updatedAt": "2024-01-15T10:00:00"
    }
  ]
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/v1/product
```

---

#### 2. Get Product by ID

**GET** `/api/v1/product/{id}`

**Public Endpoint** - No authentication required

**Path Parameters:**
- `id` (Long) - Product ID

**Response:**
```json
{
  "status": "success",
  "message": "Get product success",
  "data": {
    "id": 1,
    "name": "iPhone 15 Pro",
    "description": "Latest iPhone with A17 chip",
    "price": 999.99,
    "stock": 50,
    "imgUrl": "https://cdn.example.com/products/iphone15.jpg",
    "categoryId": 1,
    "brandId": 1,
    "createdAt": "2024-01-10T09:00:00",
    "updatedAt": "2024-01-15T10:00:00"
  }
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/v1/product/1
```

---

#### 3. Create Product (Admin Only)

**POST** `/api/v1/product`

**Protected Endpoint** - Requires JWT token with ADMIN role

**Headers:**
```
Authorization: Bearer <your_admin_access_token>
```

**Request Body:**
```json
{
  "name": "iPhone 15 Pro",
  "description": "Latest iPhone with A17 chip",
  "price": 999.99,
  "stock": 50,
  "imgUrl": "https://cdn.example.com/products/iphone15.jpg",
  "categoryId": 1,
  "brandId": 1
}
```

**Response:** (Same as Get Product response)

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/v1/product \
  -H "Authorization: Bearer your_admin_token_here" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"iPhone 15 Pro\",\"description\":\"Latest iPhone\",\"price\":999.99,\"stock\":50,\"categoryId\":1,\"brandId\":1}"
```

---

#### 4. Update Product (Admin Only)

**PUT** `/api/v1/product/{id}`

**Protected Endpoint** - Requires JWT token with ADMIN role

**Path Parameters:**
- `id` (Long) - Product ID

**Request Body:** (Same as Create Product)

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/v1/product/1 \
  -H "Authorization: Bearer your_admin_token_here" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"iPhone 15 Pro Max\",\"price\":1199.99}"
```

---

#### 5. Delete Product (Admin Only)

**DELETE** `/api/v1/product/{id}`

**Protected Endpoint** - Requires JWT token with ADMIN role

**Path Parameters:**
- `id` (Long) - Product ID

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/v1/product/1 \
  -H "Authorization: Bearer your_admin_token_here"
```

---

### Brands Endpoints

#### 1. Get All Brands

**GET** `/api/v1/brand`

**Public Endpoint** - No authentication required

**Response:**
```json
{
  "status": "success",
  "message": "Get all brands success",
  "data": [
    {
      "id": 1,
      "name": "Apple",
      "description": "Think Different",
      "logoUrl": "https://cdn.example.com/brands/apple.jpg",
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00"
    }
  ]
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/v1/brand
```

---

#### 2. Get Brand by ID

**GET** `/api/v1/brand/{id}`

**Public Endpoint** - No authentication required

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/v1/brand/1
```

---

#### 3. Create Brand (Admin Only)

**POST** `/api/v1/brand`

**Protected Endpoint** - Requires JWT token with ADMIN role

**Request Body:**
```json
{
  "name": "Apple",
  "description": "Think Different",
  "logoUrl": "https://cdn.example.com/brands/apple.jpg"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/v1/brand \
  -H "Authorization: Bearer your_admin_token_here" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Apple\",\"description\":\"Think Different\"}"
```

---

#### 4. Update Brand (Admin Only)

**PUT** `/api/v1/brand/{id}`

**Protected Endpoint** - Requires JWT token with ADMIN role

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/v1/brand/1 \
  -H "Authorization: Bearer your_admin_token_here" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Apple Inc.\",\"description\":\"Innovation at its best\"}"
```

---

#### 5. Delete Brand (Admin Only)

**DELETE** `/api/v1/brand/{id}`

**Protected Endpoint** - Requires JWT token with ADMIN role

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/v1/brand/1 \
  -H "Authorization: Bearer your_admin_token_here"
```

---

### Categories Endpoints

#### 1. Get All Categories

**GET** `/api/v1/category`

**Public Endpoint** - No authentication required

**Response:**
```json
{
  "status": "success",
  "message": "Get all categories success",
  "data": [
    {
      "id": 1,
      "name": "Smartphones",
      "description": "Mobile phones and accessories",
      "imgUrl": "https://cdn.example.com/categories/smartphones.jpg",
      "createdAt": "2024-01-01T00:00:00",
      "updatedAt": "2024-01-01T00:00:00"
    }
  ]
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/v1/category
```

---

#### 2. Get Category by ID

**GET** `/api/v1/category/{id}`

**Public Endpoint** - No authentication required

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/v1/category/1
```

---

#### 3. Create Category (Admin Only)

**POST** `/api/v1/category`

**Protected Endpoint** - Requires JWT token with ADMIN role

**Request Body:**
```json
{
  "name": "Smartphones",
  "description": "Mobile phones and accessories",
  "imgUrl": "https://cdn.example.com/categories/smartphones.jpg"
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/v1/category \
  -H "Authorization: Bearer your_admin_token_here" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Smartphones\",\"description\":\"Mobile phones\"}"
```

---

#### 4. Update Category (Admin Only)

**PUT** `/api/v1/category/{id}`

**Protected Endpoint** - Requires JWT token with ADMIN role

**cURL Example:**
```bash
curl -X PUT http://localhost:8080/api/v1/category/1 \
  -H "Authorization: Bearer your_admin_token_here" \
  -H "Content-Type: application/json" \
  -d "{\"name\":\"Mobile Devices\"}"
```

---

#### 5. Delete Category (Admin Only)

**DELETE** `/api/v1/category/{id}`

**Protected Endpoint** - Requires JWT token with ADMIN role

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/v1/category/1 \
  -H "Authorization: Bearer your_admin_token_here"
```

---

### Image Upload Endpoints

All image upload endpoints are **protected** and require JWT authentication.

#### 1. Upload Single Image

**POST** `/api/v1/images/upload`

**Protected Endpoint** - Requires JWT token

**Headers:**
```
Authorization: Bearer <your_access_token>
Content-Type: multipart/form-data
```

**Form Data Parameters:**
- `image` (file) - Image file (required)
- `category` (string) - Image category: product, user, brand, category (default: "general")
- `alt` (string) - Alt text for the image (optional)

**Supported Formats:** JPG, PNG, GIF, WEBP

**Response:**
```json
{
  "status": "success",
  "message": "Image uploaded successfully",
  "data": {
    "id": "abc123xyz",
    "path": "product/abc123xyz.jpg",
    "url": "http://52.77.101.54:3000/uploads/product/abc123xyz.jpg",
    "category": "product",
    "uploadedAt": "2024-01-15T10:30:00",
    "thumbnails": {
      "small": "http://52.77.101.54:3000/uploads/product/thumbs/small_abc123xyz.jpg",
      "medium": "http://52.77.101.54:3000/uploads/product/thumbs/medium_abc123xyz.jpg",
      "large": "http://52.77.101.54:3000/uploads/product/thumbs/large_abc123xyz.jpg"
    },
    "metadata": {
      "originalName": "iphone15.jpg",
      "mimeType": "image/jpeg",
      "size": 245678,
      "width": 1920,
      "height": 1080,
      "format": "jpeg"
    }
  }
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/v1/images/upload \
  -H "Authorization: Bearer your_access_token_here" \
  -F "image=@/path/to/your/image.jpg" \
  -F "category=product" \
  -F "alt=iPhone 15 Pro"
```

**JavaScript (Fetch) Example:**
```javascript
const formData = new FormData();
formData.append('image', fileInput.files[0]);
formData.append('category', 'product');
formData.append('alt', 'Product image');

fetch('http://localhost:8080/api/v1/images/upload', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${accessToken}`
  },
  body: formData
})
.then(response => response.json())
.then(data => console.log(data));
```

---

#### 2. Upload Multiple Images

**POST** `/api/v1/images/upload-multiple`

**Protected Endpoint** - Requires JWT token

**Form Data Parameters:**
- `images` (file[]) - Array of image files (max 10, required)
- `category` (string) - Image category for all files (default: "general")

**Response:**
```json
{
  "status": "success",
  "message": "Images uploaded successfully",
  "data": [
    {
      "id": "abc123",
      "url": "http://52.77.101.54:3000/uploads/product/abc123.jpg",
      ...
    },
    {
      "id": "def456",
      "url": "http://52.77.101.54:3000/uploads/product/def456.jpg",
      ...
    }
  ]
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/v1/images/upload-multiple \
  -H "Authorization: Bearer your_access_token_here" \
  -F "images=@/path/to/image1.jpg" \
  -F "images=@/path/to/image2.jpg" \
  -F "category=product"
```

**JavaScript Example:**
```javascript
const formData = new FormData();
// Add multiple files
for (let file of fileInput.files) {
  formData.append('images', file);
}
formData.append('category', 'product');

fetch('http://localhost:8080/api/v1/images/upload-multiple', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${accessToken}`
  },
  body: formData
})
.then(response => response.json())
.then(data => console.log(data));
```

---

#### 3. Delete Image (Admin Only)

**DELETE** `/api/v1/images/{path}`

**Protected Endpoint** - Requires JWT token with ADMIN role

**Path Parameters:**
- `path` (string) - Image path from CDN (e.g., "product/abc123xyz.jpg")

**cURL Example:**
```bash
curl -X DELETE http://localhost:8080/api/v1/images/product/abc123xyz.jpg \
  -H "Authorization: Bearer your_admin_token_here"
```

---

## Common Use Cases

### Use Case 1: User Registration and Login

```javascript
// 1. Register
const registerResponse = await fetch('http://localhost:8080/api/auth/register', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'johndoe',
    password: 'SecurePass123!',
    email: 'john@example.com',
    fullName: 'John Doe'
  })
});

// 2. Login
const loginResponse = await fetch('http://localhost:8080/api/auth/login', {
  method: 'POST',
  headers: { 'Content-Type': 'application/json' },
  body: JSON.stringify({
    username: 'johndoe',
    password: 'SecurePass123!'
  })
});

const { accessToken, refreshToken } = await loginResponse.json();

// 3. Store tokens (localStorage, sessionStorage, or secure cookie)
localStorage.setItem('accessToken', accessToken);
localStorage.setItem('refreshToken', refreshToken);
```

---

### Use Case 2: Browse Products (No Auth)

```javascript
// Get all products
const products = await fetch('http://localhost:8080/api/v1/product')
  .then(res => res.json());

// Get product details
const product = await fetch('http://localhost:8080/api/v1/product/1')
  .then(res => res.json());

// Get all brands
const brands = await fetch('http://localhost:8080/api/v1/brand')
  .then(res => res.json());

// Get all categories
const categories = await fetch('http://localhost:8080/api/v1/category')
  .then(res => res.json());
```

---

### Use Case 3: Update User Profile with New Avatar

```javascript
// 1. Upload new avatar
const formData = new FormData();
formData.append('image', avatarFile);
formData.append('category', 'user');

const uploadResponse = await fetch('http://localhost:8080/api/v1/images/upload', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${accessToken}`
  },
  body: formData
});

const { data: imageData } = await uploadResponse.json();

// 2. Update profile with new image URL
const updateResponse = await fetch('http://localhost:8080/api/v1/user/profile', {
  method: 'PUT',
  headers: {
    'Authorization': `Bearer ${accessToken}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    imgUrl: imageData.url
  })
});
```

---

### Use Case 4: Admin - Create Product with Image

```javascript
// 1. Upload product image
const formData = new FormData();
formData.append('image', productImageFile);
formData.append('category', 'product');

const uploadResponse = await fetch('http://localhost:8080/api/v1/images/upload', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${adminToken}`
  },
  body: formData
});

const { data: imageData } = await uploadResponse.json();

// 2. Create product with image URL
const productResponse = await fetch('http://localhost:8080/api/v1/product', {
  method: 'POST',
  headers: {
    'Authorization': `Bearer ${adminToken}`,
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    name: 'iPhone 15 Pro',
    description: 'Latest iPhone',
    price: 999.99,
    stock: 50,
    imgUrl: imageData.url,
    categoryId: 1,
    brandId: 1
  })
});
```

---

### Use Case 5: Token Refresh

```javascript
async function refreshAccessToken() {
  const refreshToken = localStorage.getItem('refreshToken');
  
  const response = await fetch('http://localhost:8080/api/auth/refresh', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ refreshToken })
  });
  
  if (response.ok) {
    const { accessToken, refreshToken: newRefreshToken } = await response.json();
    localStorage.setItem('accessToken', accessToken);
    localStorage.setItem('refreshToken', newRefreshToken);
    return accessToken;
  } else {
    // Refresh token expired, redirect to login
    window.location.href = '/login';
  }
}

// Use in fetch interceptor
async function authenticatedFetch(url, options = {}) {
  const accessToken = localStorage.getItem('accessToken');
  
  const response = await fetch(url, {
    ...options,
    headers: {
      ...options.headers,
      'Authorization': `Bearer ${accessToken}`
    }
  });
  
  if (response.status === 401) {
    // Token expired, refresh and retry
    const newToken = await refreshAccessToken();
    return fetch(url, {
      ...options,
      headers: {
        ...options.headers,
        'Authorization': `Bearer ${newToken}`
      }
    });
  }
  
  return response;
}
```

---

## Quick Reference Table

### Endpoint Access Control

| Endpoint | Method | Public | User | Admin |
|----------|--------|--------|------|-------|
| `/api/auth/register` | POST | ✅ | ✅ | ✅ |
| `/api/auth/login` | POST | ✅ | ✅ | ✅ |
| `/api/auth/refresh` | POST | ✅ | ✅ | ✅ |
| `/api/auth/test` | GET | ❌ | ✅ | ✅ |
| `/api/v1/user/profile` | GET | ❌ | ✅ | ✅ |
| `/api/v1/user/profile` | PUT | ❌ | ✅ | ✅ |
| `/api/v1/user/{id}` | GET | ❌ | ✅ | ✅ |
| `/api/v1/product` | GET | ✅ | ✅ | ✅ |
| `/api/v1/product/{id}` | GET | ✅ | ✅ | ✅ |
| `/api/v1/product` | POST | ❌ | ❌ | ✅ |
| `/api/v1/product/{id}` | PUT | ❌ | ❌ | ✅ |
| `/api/v1/product/{id}` | DELETE | ❌ | ❌ | ✅ |
| `/api/v1/brand` | GET | ✅ | ✅ | ✅ |
| `/api/v1/brand/{id}` | GET | ✅ | ✅ | ✅ |
| `/api/v1/brand` | POST | ❌ | ❌ | ✅ |
| `/api/v1/brand/{id}` | PUT | ❌ | ❌ | ✅ |
| `/api/v1/brand/{id}` | DELETE | ❌ | ❌ | ✅ |
| `/api/v1/category` | GET | ✅ | ✅ | ✅ |
| `/api/v1/category/{id}` | GET | ✅ | ✅ | ✅ |
| `/api/v1/category` | POST | ❌ | ❌ | ✅ |
| `/api/v1/category/{id}` | PUT | ❌ | ❌ | ✅ |
| `/api/v1/category/{id}` | DELETE | ❌ | ❌ | ✅ |
| `/api/v1/images/upload` | POST | ❌ | ✅ | ✅ |
| `/api/v1/images/upload-multiple` | POST | ❌ | ✅ | ✅ |
| `/api/v1/images/{path}` | DELETE | ❌ | ❌ | ✅ |

---

## Contact & Support

For questions or issues, please contact the backend development team.

**API Version:** 1.0  
**Last Updated:** January 2024
