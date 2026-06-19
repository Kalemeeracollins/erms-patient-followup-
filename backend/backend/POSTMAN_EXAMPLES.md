# Hospital Patient Follow-Up System - Postman Examples

This document provides ready-to-use Postman requests for testing the API.

## Environment Setup

### Variables to Add in Postman Environment:

```
{
  "baseUrl": "http://localhost:8080/api",
  "accessToken": "",
  "refreshToken": ""
}
```

JavaScript code to automatically set tokens after login:

```javascript
var jsonData = pm.response.json();
pm.environment.set("accessToken", jsonData.accessToken);
pm.environment.set("refreshToken", jsonData.refreshToken);
```

---

## Authentication Requests

### 1. Register New User

```http
POST {{baseUrl}}/auth/register
Content-Type: application/json

{
  "username": "doctor123",
  "email": "doctor123@example.com",
  "fullName": "Dr. John Doe",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "phoneNumber": "+1-555-0123",
  "role": "DOCTOR"
}
```

**Tests Tab (Postman):**
```javascript
if (pm.response.code === 201) {
    var jsonData = pm.response.json();
    pm.environment.set("accessToken", jsonData.accessToken);
    pm.environment.set("refreshToken", jsonData.refreshToken);
    pm.test("User registered and tokens set", function () {
        pm.expect(jsonData.user.username).to.equal("doctor123");
    });
}
```

---

### 2. Login

```http
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
  "username": "doctor123",
  "password": "SecurePass123!"
}
```

**Tests Tab (Postman):**
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("accessToken", jsonData.accessToken);
    pm.environment.set("refreshToken", jsonData.refreshToken);
    pm.test("Login successful", function () {
        pm.expect(jsonData.user.username).to.equal("doctor123");
    });
}
```

---

### 3. Refresh Access Token

```http
POST {{baseUrl}}/auth/refresh
Content-Type: application/json

{
  "refreshToken": "{{refreshToken}}"
}
```

**Tests Tab (Postman):**
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("accessToken", jsonData.accessToken);
    pm.test("Token refreshed successfully", function () {
        pm.expect(jsonData.accessToken).to.be.truthy;
    });
}
```

---

### 4. Get Current User

```http
GET {{baseUrl}}/auth/me
Authorization: Bearer {{accessToken}}
```

---

### 5. Logout

```http
POST {{baseUrl}}/auth/logout
Authorization: Bearer {{accessToken}}
```

---

## User Management Requests

### 1. Get All Users (Admin Only)

```http
GET {{baseUrl}}/users
Authorization: Bearer {{accessToken}}
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "username": "admin",
    "fullName": "Administrator",
    "email": "admin@example.com",
    "phoneNumber": "+1-555-0000",
    "role": "ADMIN",
    "active": true,
    "createdAt": "2024-01-10T08:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  },
  {
    "id": 2,
    "username": "doctor123",
    "fullName": "Dr. John Doe",
    "email": "doctor123@example.com",
    "phoneNumber": "+1-555-0123",
    "role": "DOCTOR",
    "active": true,
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

---

### 2. Get User by ID

```http
GET {{baseUrl}}/users/2
Authorization: Bearer {{accessToken}}
```

**Expected Response (200 OK):**
```json
{
  "id": 2,
  "username": "doctor123",
  "fullName": "Dr. John Doe",
  "email": "doctor123@example.com",
  "phoneNumber": "+1-555-0123",
  "role": "DOCTOR",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

---

### 3. Get User by Username

```http
GET {{baseUrl}}/users/username/doctor123
Authorization: Bearer {{accessToken}}
```

---

### 4. Update User

```http
PUT {{baseUrl}}/users/2
Authorization: Bearer {{accessToken}}
Content-Type: application/json

{
  "fullName": "Dr. John Smith",
  "email": "john.smith@example.com",
  "phoneNumber": "+1-555-9999"
}
```

**Expected Response (200 OK):**
```json
{
  "id": 2,
  "username": "doctor123",
  "fullName": "Dr. John Smith",
  "email": "john.smith@example.com",
  "phoneNumber": "+1-555-9999",
  "role": "DOCTOR",
  "active": true,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T11:30:00"
}
```

---

### 5. Activate User

```http
PUT {{baseUrl}}/users/2/activate
Authorization: Bearer {{accessToken}}
```

---

### 6. Deactivate User

```http
PUT {{baseUrl}}/users/2/deactivate
Authorization: Bearer {{accessToken}}
```

---

### 7. Delete User

```http
DELETE {{baseUrl}}/users/2
Authorization: Bearer {{accessToken}}
```

---

## Testing Error Scenarios

### 1. Invalid Login Credentials

```http
POST {{baseUrl}}/auth/login
Content-Type: application/json

{
  "username": "doctor123",
  "password": "WrongPassword"
}
```

**Expected Response (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T11:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Invalid username or password",
  "path": "/auth/login"
}
```

---

### 2. Duplicate Username Registration

```http
POST {{baseUrl}}/auth/register
Content-Type: application/json

{
  "username": "doctor123",
  "email": "another@example.com",
  "fullName": "Another User",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "phoneNumber": "+1-555-0456",
  "role": "DOCTOR"
}
```

**Expected Response (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T11:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Username already exists",
  "path": "/auth/register"
}
```

---

### 3. Password Mismatch

```http
POST {{baseUrl}}/auth/register
Content-Type: application/json

{
  "username": "nurse123",
  "email": "nurse123@example.com",
  "fullName": "Nurse Jane",
  "password": "SecurePass123!",
  "confirmPassword": "DifferentPass123!",
  "phoneNumber": "+1-555-0789",
  "role": "NURSE"
}
```

**Expected Response (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T11:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Passwords do not match",
  "path": "/auth/register"
}
```

---

### 4. Validation Errors

```http
POST {{baseUrl}}/auth/register
Content-Type: application/json

{
  "username": "ab",
  "email": "invalid-email",
  "fullName": "A",
  "password": "123",
  "confirmPassword": "123",
  "phoneNumber": "",
  "role": "INVALID_ROLE"
}
```

**Expected Response (400 Bad Request):**
```json
{
  "timestamp": "2024-01-15T11:00:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation failed",
  "errors": {
    "username": "Username must be between 3 and 50 characters",
    "email": "Email should be valid",
    "fullName": "Full name must be between 2 and 100 characters",
    "password": "Password must be between 6 and 100 characters",
    "role": "Invalid role provided"
  },
  "path": "/auth/register"
}
```

---

### 5. Unauthorized Access (Missing Token)

```http
GET {{baseUrl}}/users
```

**Expected Response (401 Unauthorized):**
```json
{
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/users"
}
```

---

### 6. Resource Not Found

```http
GET {{baseUrl}}/users/999
Authorization: Bearer {{accessToken}}
```

**Expected Response (404 Not Found):**
```json
{
  "timestamp": "2024-01-15T11:00:00",
  "status": 404,
  "error": "Not Found",
  "message": "User not found with ID: 999",
  "path": "/users/999"
}
```

---

## Test Workflow

Use this workflow to test the complete authentication flow:

1. **Register a new user** - POST /auth/register
   - Verify response contains accessToken and refreshToken
   - Verify user data is correct

2. **Login** - POST /auth/login
   - Verify response contains new tokens
   - Verify tokens are set in environment

3. **Get current user** - GET /auth/me
   - Verify authenticated user information

4. **Get all users** - GET /users
   - Verify list contains registered user
   - Verify user fields are populated

5. **Update user** - PUT /users/{id}
   - Verify user fields are updated correctly
   - Verify updatedAt timestamp is changed

6. **Refresh token** - POST /auth/refresh
   - Verify new accessToken is returned
   - Verify old token can no longer be used

7. **Logout** - POST /auth/logout
   - Verify logout message

---

## Bulk Testing Scripts

### Create Multiple Users (Postman Pre-request Script)

```javascript
const roles = ["ADMIN", "DOCTOR", "NURSE", "RECEPTIONIST"];
const userNumber = Math.floor(Math.random() * 10000);

pm.environment.set("testUsername", `user${userNumber}`);
pm.environment.set("testEmail", `user${userNumber}@example.com`);
pm.environment.set("testRole", roles[Math.floor(Math.random() * roles.length)]);
```

### Performance Testing

Use Postman's Runner to execute multiple requests sequentially:

1. Set up iterations (e.g., 100)
2. Run collection with 100ms delay between requests
3. Monitor response times in the results summary

---

## Notes

- Replace `{{baseUrl}}`, `{{accessToken}}`, and `{{refreshToken}}` with actual values
- All timestamps are in ISO 8601 format
- Phone numbers should follow international format
- Passwords must be between 6-100 characters
- Usernames must be unique and between 3-50 characters
- Emails must be unique and valid format
