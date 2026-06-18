# Quick Reference Guide - Hospital Patient Follow-Up System

## 🚀 Quick Start (5 Minutes)

### 1. Setup Database
```bash
# MySQL Command Line
mysql -u root -p
CREATE DATABASE hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
exit
```

### 2. Configure App
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password
app.jwt.secret=generate-with-openssl-rand-base64-32
```

### 3. Run Application
```bash
mvn clean install
mvn spring-boot:run
```

### 4. Test Login
```bash
# Register
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username":"doctor1",
    "email":"doctor@hospital.com",
    "fullName":"Dr. Smith",
    "password":"SecurePass123!",
    "confirmPassword":"SecurePass123!",
    "role":"DOCTOR"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"doctor1","password":"SecurePass123!"}'
```

---

## 📊 API Quick Reference

### Authentication
```
POST /api/auth/register        → Register new user
POST /api/auth/login           → Login and get tokens
POST /api/auth/refresh         → Refresh access token
GET  /api/auth/me              → Get current user
POST /api/auth/logout          → Logout
```

### User Management (Admin Only)
```
GET    /api/users              → List all users
GET    /api/users/{id}         → Get user details
PUT    /api/users/{id}         → Update user
PUT    /api/users/{id}/activate    → Activate user
PUT    /api/users/{id}/deactivate  → Deactivate user
DELETE /api/users/{id}         → Delete user
```

---

## 🔐 Roles & Permissions

| Role | Permissions |
|------|------------|
| **ADMIN** | Full system access, manage users, view reports |
| **DOCTOR** | View patients, create visits, manage follow-ups |
| **NURSE** | View patients, view follow-ups |
| **RECEPTIONIST** | Register patients, update patient info, schedule follow-ups |

---

## 📝 Request/Response Examples

### Register User
**Request:**
```json
{
  "username": "doctor1",
  "email": "doctor@hospital.com",
  "fullName": "Dr. John Smith",
  "password": "SecurePass123!",
  "confirmPassword": "SecurePass123!",
  "phoneNumber": "+1-555-0123",
  "role": "DOCTOR"
}
```

**Response (201):**
```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "eyJhbGci...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": {
    "id": 1,
    "username": "doctor1",
    "fullName": "Dr. John Smith",
    "email": "doctor@hospital.com",
    "role": "DOCTOR"
  }
}
```

### Login User
**Request:**
```json
{
  "username": "doctor1",
  "password": "SecurePass123!"
}
```

**Response (200):**
```json
{
  "accessToken": "eyJhbGci...",
  "refreshToken": "eyJhbGci...",
  "tokenType": "Bearer",
  "expiresIn": 86400,
  "user": { ... }
}
```

### Get All Users
**Request:**
```
GET /api/users
Authorization: Bearer <access_token>
```

**Response (200):**
```json
[
  {
    "id": 1,
    "username": "doctor1",
    "fullName": "Dr. John Smith",
    "email": "doctor@hospital.com",
    "role": "DOCTOR",
    "active": true
  }
]
```

### Update User
**Request:**
```json
PUT /api/users/1
Authorization: Bearer <access_token>

{
  "fullName": "Dr. John Smith Jr.",
  "email": "newmail@hospital.com",
  "phoneNumber": "+1-555-9999"
}
```

**Response (200):**
```json
{
  "id": 1,
  "fullName": "Dr. John Smith Jr.",
  "email": "newmail@hospital.com",
  ...
}
```

---

## 🛠️ Common Commands

### Database Setup
```bash
# Create database
mysql -u root -p < create_db.sql

# View tables
mysql -u root -p hospital_db -e "SHOW TABLES;"

# Check user table
mysql -u root -p hospital_db -e "DESC users;"
```

### Maven Commands
```bash
# Build project
mvn clean install

# Run application
mvn spring-boot:run

# Run tests
mvn test

# Package application
mvn clean package
```

### Generate JWT Secret
```bash
# Linux/Mac
openssl rand -base64 32

# Windows PowerShell
[Convert]::ToBase64String((1..32|ForEach-Object{[byte](Get-Random -Max 256)}))
```

---

## 🔧 Troubleshooting

| Problem | Solution |
|---------|----------|
| "Connection refused" | Ensure MySQL is running on localhost:3306 |
| "Database not found" | Create database: `CREATE DATABASE hospital_db;` |
| "JWT secret not set" | Add `app.jwt.secret` to application.properties |
| "Port 8080 in use" | Change port in application.properties or kill process |
| "CORS error" | Update `spring.mvc.cors.allowed-origins` in properties |
| "401 Unauthorized" | Ensure token is included in Authorization header |
| "403 Forbidden" | Check user role has permission for endpoint |

---

## 📂 Key Files Location

```
src/main/java/com/example/backend/
├── auth/
│   ├── AuthController.java          ← Register/Login endpoints
│   ├── AuthService.java             ← Auth business logic
│   └── dto/
│       ├── LoginRequest.java
│       ├── LoginResponse.java
│       └── RegisterRequest.java
├── user/
│   ├── UserController.java          ← User management endpoints
│   ├── UserService.java
│   ├── User.java                    ← User entity
│   ├── UserRepository.java
│   └── dto/
│       └── UserDTO.java
├── security/
│   ├── SecurityConfig.java          ← Main security configuration
│   ├── JwtService.java              ← JWT token handling
│   ├── JwtAuthenticationFilter.java
│   ├── CustomUserDetailsService.java
│   └── JwtAuthenticationEntryPoint.java
├── common/exception/
│   ├── GlobalExceptionHandler.java  ← Error handling
│   ├── BadRequestException.java
│   └── ResourceNotFoundException.java
└── audit/
    └── AuditService.java            ← Audit logging

src/main/resources/
└── application.properties            ← Configuration
```

---

## 💾 Environment Variables

**Set these before running in production:**

```bash
# Database
export DB_URL=jdbc:mysql://localhost:3306/hospital_db
export DB_USERNAME=root
export DB_PASSWORD=your_secure_password

# JWT
export JWT_SECRET=your_random_32_char_secret_key
export JWT_EXPIRATION=86400000
export JWT_REFRESH_EXPIRATION=604800000

# Server
export SERVER_PORT=8080
export CORS_ALLOWED_ORIGINS=https://yourdomain.com
```

---

## 🧪 Testing with Postman

### Import Collection
1. Copy requests from `POSTMAN_EXAMPLES.md`
2. Create new Postman collection
3. Add environment variables:
   - `baseUrl` = `http://localhost:8080/api`
   - `accessToken` = (set after login)
   - `refreshToken` = (set after login)

### Test Flow
1. **POST /auth/register** → Get tokens
2. **POST /auth/login** → Get fresh tokens
3. **GET /auth/me** → Verify authentication
4. **GET /users** → Test authorization
5. **PUT /users/{id}** → Test updates
6. **POST /auth/refresh** → Test token refresh

---

## 🚀 Integration with React

### Install Dependencies
```bash
npm install axios react-router-dom zustand
```

### Create API Service
```javascript
// src/services/api.js
import axios from 'axios';

const api = axios.create({
  baseURL: 'http://localhost:8080/api'
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

export default api;
```

### Create Auth Store (Zustand)
```javascript
// src/store/authStore.js
import create from 'zustand';
import api from '../services/api';

export const useAuthStore = create((set) => ({
  user: null,
  login: async (username, password) => {
    const { data } = await api.post('/auth/login', { username, password });
    localStorage.setItem('accessToken', data.accessToken);
    localStorage.setItem('refreshToken', data.refreshToken);
    set({ user: data.user });
  }
}));
```

---

## 🔐 Security Checklist

- [ ] Set strong JWT secret (32+ characters, random)
- [ ] Enable HTTPS in production
- [ ] Configure CORS for specific domains only
- [ ] Use environment variables for secrets
- [ ] Enable database user authentication
- [ ] Regular security updates
- [ ] Monitor login attempts
- [ ] Set up intrusion detection
- [ ] Enable audit logging
- [ ] Regular database backups

---

## 📊 Performance Tips

1. **Add Caching** - Cache user lookups with Redis
2. **Database Optimization** - Use existing indexes wisely
3. **Connection Pooling** - Already configured with HikariCP
4. **Rate Limiting** - Add for login endpoint
5. **Pagination** - For user list endpoints
6. **Async Processing** - For audit logging

---

## 🎓 Learning Resources

### File to Read First
1. **README.md** - Overview and quick start
2. **API_DOCUMENTATION.md** - Full API reference
3. **PROJECT_STRUCTURE.md** - Architecture details

### Implementation Guides
- **POSTMAN_EXAMPLES.md** - For API testing
- **REACT_INTEGRATION.md** - For frontend integration
- **IMPLEMENTATION_SUMMARY.md** - For what was built

---

## ✅ Deployment Checklist

- [ ] Update database credentials
- [ ] Generate strong JWT secret
- [ ] Configure HTTPS/SSL
- [ ] Set up CORS for production domains
- [ ] Enable external logging
- [ ] Set up monitoring
- [ ] Configure backups
- [ ] Enable rate limiting
- [ ] Test all endpoints
- [ ] Security audit
- [ ] Load testing
- [ ] Documentation review

---

## 📞 Quick Help

### Where to find...
- **API endpoints** → See this file or API_DOCUMENTATION.md
- **How to integrate React** → REACT_INTEGRATION.md
- **Project structure** → PROJECT_STRUCTURE.md
- **Code architecture** → Look at file organization
- **Database schema** → application.properties or PROJECT_STRUCTURE.md
- **Configuration options** → application.properties comments

---

## 🎯 Next Steps

1. ✅ **Run the application** following Quick Start section
2. ✅ **Test endpoints** using Postman examples
3. ✅ **Read full documentation** for detailed information
4. ✅ **Integrate with React** using the integration guide
5. ✅ **Deploy to production** following deployment checklist

---

**Need more details? Check the full documentation files!**
- 📘 README.md
- 📗 API_DOCUMENTATION.md
- 📙 POSTMAN_EXAMPLES.md
- 📕 REACT_INTEGRATION.md
- 📓 PROJECT_STRUCTURE.md
- 📔 IMPLEMENTATION_SUMMARY.md
