# Hospital Patient Follow-Up System - API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication

All endpoints except `/auth/login`, `/auth/register`, and `/auth/refresh` require JWT Bearer token in Authorization header:

```
Authorization: Bearer <your_jwt_token>
```

## Authentication Endpoints

### 1. User Login

**Endpoint:** `POST /auth/login`

**Description:** Authenticate user with username and password

**Request Body:**
```json
{
  "username": "doctor123",
  "password": "securePassword123"
}
```

**Success Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "username": "doctor123",
    "fullName": "Dr. John Doe",
    "email": "john@example.com",
    "role": "DOCTOR"
  }
}
```

**Error Response (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid username or password",
  "path": "/auth/login"
}
```

---

### 2. User Registration

**Endpoint:** `POST /auth/register`

**Description:** Register new user account

**Request Body:**
```json
{
  "username": "doctor123",
  "email": "john@example.com",
  "fullName": "Dr. John Doe",
  "password": "securePassword123",
  "confirmPassword": "securePassword123",
  "phoneNumber": "+1-555-0123",
  "role": "DOCTOR"
}
```

**Role Options:**
- `ADMIN` - System Administrator
- `DOCTOR` - Doctor
- `NURSE` - Nurse
- `RECEPTIONIST` - Receptionist

**Success Response (201 Created):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 5,
    "username": "doctor123",
    "fullName": "Dr. John Doe",
    "email": "john@example.com",
    "role": "DOCTOR"
  }
}
```

**Validation Errors (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "errors": {
    "username": "Username must be between 3 and 50 characters",
    "password": "Password must be between 6 and 100 characters",
    "email": "Email should be valid"
  },
  "path": "/auth/register"
}
```

---

### 3. Refresh Access Token

**Endpoint:** `POST /auth/refresh`

**Description:** Generate new access token using refresh token

**Request Body:**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Success Response (200 OK):**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer"
}
```

---

### 4. Get Current User

**Endpoint:** `GET /auth/me`

**Description:** Get authenticated user information

**Headers:**
```
Authorization: Bearer <access_token>
```

**Success Response (200 OK):**
```json
{
  "username": "doctor123",
  "authorities": [
    {
      "authority": "ROLE_DOCTOR"
    }
  ]
}
```

---

### 5. Logout

**Endpoint:** `POST /auth/logout`

**Description:** Logout current user (clears server-side context)

**Headers:**
```
Authorization: Bearer <access_token>
```

**Success Response (200 OK):**
```json
{
  "message": "Logged out successfully"
}
```

---

## User Management Endpoints

### 1. Get All Users

**Endpoint:** `GET /users`

**Description:** Get list of all users (Admin only)

**Headers:**
```
Authorization: Bearer <access_token>
```

**Success Response (200 OK):**
```json
[
  {
    "id": 1,
    "username": "doctor123",
    "fullName": "Dr. John Doe",
    "email": "john@example.com",
    "phoneNumber": "+1-555-0123",
    "role": "DOCTOR",
    "active": true,
    "createdAt": "2024-01-10T08:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "username": "nurse456",
    "fullName": "Jane Smith",
    "email": "jane@example.com",
    "phoneNumber": "+1-555-0124",
    "role": "NURSE",
    "active": true,
    "createdAt": "2024-01-11T09:15:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

---

### 2. Get User by ID

**Endpoint:** `GET /users/{id}`

**Description:** Get user information by ID (Admin only)

**Path Parameters:**
- `id` (number) - User ID

**Headers:**
```
Authorization: Bearer <access_token>
```

**Success Response (200 OK):**
```json
{
  "id": 1,
  "username": "doctor123",
  "fullName": "Dr. John Doe",
  "email": "john@example.com",
  "phoneNumber": "+1-555-0123",
  "role": "DOCTOR",
  "active": true,
  "createdAt": "2024-01-10T08:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

**Error Response (404 Not Found):**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with ID: 999",
  "path": "/users/999"
}
```

---

### 3. Get User by Username

**Endpoint:** `GET /users/username/{username}`

**Description:** Get user information by username (Admin only)

**Path Parameters:**
- `username` (string) - Username

**Headers:**
```
Authorization: Bearer <access_token>
```

**Success Response (200 OK):**
```json
{
  "id": 1,
  "username": "doctor123",
  "fullName": "Dr. John Doe",
  "email": "john@example.com",
  "phoneNumber": "+1-555-0123",
  "role": "DOCTOR",
  "active": true,
  "createdAt": "2024-01-10T08:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 4. Update User

**Endpoint:** `PUT /users/{id}`

**Description:** Update user information (Admin only)

**Path Parameters:**
- `id` (number) - User ID

**Headers:**
```
Authorization: Bearer <access_token>
Content-Type: application/json
```

**Request Body:**
```json
{
  "fullName": "Dr. John Smith",
  "email": "john.smith@example.com",
  "phoneNumber": "+1-555-9999"
}
```

**Success Response (200 OK):**
```json
{
  "id": 1,
  "username": "doctor123",
  "fullName": "Dr. John Smith",
  "email": "john.smith@example.com",
  "phoneNumber": "+1-555-9999",
  "role": "DOCTOR",
  "active": true,
  "createdAt": "2024-01-10T08:30:00",
  "updatedAt": "2024-01-15T11:00:00"
}
```

---

### 5. Activate User

**Endpoint:** `PUT /users/{id}/activate`

**Description:** Activate user account (Admin only)

**Path Parameters:**
- `id` (number) - User ID

**Headers:**
```
Authorization: Bearer <access_token>
```

**Success Response (200 OK):**
```json
{
  "id": 1,
  "username": "doctor123",
  "fullName": "Dr. John Doe",
  "email": "john@example.com",
  "phoneNumber": "+1-555-0123",
  "role": "DOCTOR",
  "active": true,
  "createdAt": "2024-01-10T08:30:00",
  "updatedAt": "2024-01-15T11:00:00"
}
```

---

### 6. Deactivate User

**Endpoint:** `PUT /users/{id}/deactivate`

**Description:** Deactivate user account (Admin only)

**Path Parameters:**
- `id` (number) - User ID

**Headers:**
```
Authorization: Bearer <access_token>
```

**Success Response (200 OK):**
```json
{
  "id": 1,
  "username": "doctor123",
  "fullName": "Dr. John Doe",
  "email": "john@example.com",
  "phoneNumber": "+1-555-0123",
  "role": "DOCTOR",
  "active": false,
  "createdAt": "2024-01-10T08:30:00",
  "updatedAt": "2024-01-15T11:00:00"
}
```

---

## HTTP Status Codes

- `200 OK` - Request successful
- `201 Created` - Resource created successfully
- `400 Bad Request` - Invalid request or validation failed
- `401 Unauthorized` - Missing or invalid authentication token
- `403 Forbidden` - User doesn't have permission for the resource
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Server error

---

## Database Schema (Hibernate Generated)

### Users Table
```sql
CREATE TABLE users (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(100) NOT NULL,
  email VARCHAR(100) UNIQUE NOT NULL,
  phone_number VARCHAR(20),
  role VARCHAR(20) NOT NULL,
  active BOOLEAN NOT NULL DEFAULT true,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  KEY idx_username (username),
  KEY idx_email (email),
  KEY idx_role (role)
);
```

---

## Error Response Format

All errors follow this format:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Error description",
  "path": "/api/endpoint"
}
```

For validation errors:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "errors": {
    "field1": "Error message",
    "field2": "Error message"
  },
  "path": "/api/endpoint"
}
```

---

## Security Notes

1. **JWT Token Expiration:** 24 hours (86400 seconds)
2. **Refresh Token Expiration:** 7 days (604800 seconds)
3. **Password Hashing:** BCrypt with salt
4. **CORS:** Configured for localhost:3000 and localhost:5173
5. **CSRF:** Disabled for stateless JWT authentication
6. **Session Management:** Stateless (no sessions stored)

---

## Role-Based Access Control

| Endpoint | ADMIN | DOCTOR | NURSE | RECEPTIONIST |
|----------|-------|--------|-------|--------------|
| GET /users | ✓ | ✗ | ✗ | ✗ |
| GET /users/{id} | ✓ | ✗ | ✗ | ✗ |
| PUT /users/{id} | ✓ | ✗ | ✗ | ✗ |
| GET /patients | ✓ | ✓ | ✓ | ✗ |
| POST /patients | ✓ | ✗ | ✗ | ✓ |
| POST /visits | ✓ | ✓ | ✗ | ✗ |
| GET /followups | ✓ | ✓ | ✓ | ✗ |
