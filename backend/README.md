# Hospital Patient Follow-Up System - Backend API

A production-ready Authentication and Authorization Module for a Hospital Patient Follow-Up System built with **Spring Boot 3.x**, **Spring Security 6**, **JWT**, and **MySQL**.

## 🚀 Features

### Authentication & Authorization
- ✅ JWT-based stateless authentication
- ✅ Refresh token support
- ✅ Role-based access control (RBAC)
- ✅ 4 predefined roles: ADMIN, DOCTOR, NURSE, RECEPTIONIST
- ✅ Secure password hashing using BCrypt
- ✅ Token expiration with automatic refresh

### Security
- ✅ CORS configuration for React frontend
- ✅ CSRF protection
- ✅ Input validation and sanitization
- ✅ Global exception handling
- ✅ Comprehensive audit logging
- ✅ SQL injection prevention via JPA

### Database
- ✅ MySQL 8 integration
- ✅ Hibernate automatic schema generation
- ✅ Proper indexing for performance
- ✅ Unique constraints on username and email

### API Documentation
- ✅ Complete REST API endpoints
- ✅ Postman examples and tests
- ✅ React/JavaScript integration guide
- ✅ Error response formats
- ✅ Request/response examples

## 📋 Technology Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 21 | Programming language |
| Spring Boot | 3.3.0 | Framework |
| Spring Security | 6 | Authentication/Authorization |
| JWT (JJWT) | 0.12.3 | Token generation and validation |
| MySQL | 8 | Database |
| Hibernate | Latest | ORM |
| Lombok | Latest | Code generation |
| Maven | Latest | Build tool |

## 🏗️ Project Structure

```
src/main/java/com/example/backend/
├── auth/              # Authentication & authorization
├── user/              # User management
├── security/          # JWT and security configuration
├── audit/             # Audit logging
├── common/exception/  # Exception handling
├── followup/          # (Future) Follow-up management
├── patient/           # (Future) Patient management
├── visit/             # (Future) Visit management
├── notification/      # (Future) Notifications
└── report/            # (Future) Reports
```

## 📦 Prerequisites

- **Java 21** - JDK installed
- **MySQL 8** - Database server
- **Maven 3.9+** - Build tool
- **Git** - Version control

## 🔧 Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd hospital-followup-system
```

### 2. Create MySQL Database
```sql
CREATE DATABASE hospital_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE hospital_db;
```

### 3. Configure Application Properties
Edit `src/main/resources/application.properties`:

```properties
# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
spring.datasource.username=root
spring.datasource.password=your_password

# JWT Secret (Generate with: openssl rand -base64 32)
app.jwt.secret=your-secret-key-here
```

### 4. Generate JWT Secret Key
```bash
# Linux/Mac
openssl rand -base64 32

# Windows (PowerShell)
[Convert]::ToBase64String((1..32|ForEach-Object{[byte](Get-Random -Max 256)}))
```

### 5. Build the Application
```bash
mvn clean install
```

### 6. Run the Application
```bash
mvn spring-boot:run
```

Server will start at: `http://localhost:8080`

## 🌐 API Endpoints

### Authentication
| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|----------------|
| POST | `/api/auth/login` | User login | ❌ No |
| POST | `/api/auth/register` | User registration | ❌ No |
| POST | `/api/auth/refresh` | Refresh access token | ❌ No |
| GET | `/api/auth/me` | Get current user | ✅ Yes |
| POST | `/api/auth/logout` | Logout | ✅ Yes |

### User Management (Admin Only)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/users` | Get all users |
| GET | `/api/users/{id}` | Get user by ID |
| GET | `/api/users/username/{username}` | Get user by username |
| PUT | `/api/users/{id}` | Update user |
| PUT | `/api/users/{id}/activate` | Activate user |
| PUT | `/api/users/{id}/deactivate` | Deactivate user |
| DELETE | `/api/users/{id}` | Delete user |

## 📚 Documentation Files

1. **[API_DOCUMENTATION.md](./API_DOCUMENTATION.md)** - Complete API reference
2. **[POSTMAN_EXAMPLES.md](./POSTMAN_EXAMPLES.md)** - Ready-to-use Postman requests
3. **[REACT_INTEGRATION.md](./REACT_INTEGRATION.md)** - React frontend integration guide
4. **[PROJECT_STRUCTURE.md](./PROJECT_STRUCTURE.md)** - Detailed file descriptions

## 🔐 User Roles and Permissions

### ADMIN
- Full system access
- Manage all users
- View reports

### DOCTOR
- View patients
- Create visits
- Create follow-ups
- Complete follow-ups

### NURSE
- View patients
- View follow-ups

### RECEPTIONIST
- Register patients
- Update patient information
- Schedule follow-ups

## 💡 Quick Start Example

### 1. Register a New User
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "doctor123",
    "email": "doctor@example.com",
    "fullName": "Dr. John Doe",
    "password": "SecurePass123!",
    "confirmPassword": "SecurePass123!",
    "phoneNumber": "+1-555-0123",
    "role": "DOCTOR"
  }'
```

### 2. Login
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "doctor123",
    "password": "SecurePass123!"
  }'
```

### 3. Get All Users (Admin)
```bash
curl -X GET http://localhost:8080/api/users \
  -H "Authorization: Bearer <your_access_token>"
```

## 🧪 Testing

### Using Postman
1. Import the Postman collection from `POSTMAN_EXAMPLES.md`
2. Set environment variables: `baseUrl`, `accessToken`, `refreshToken`
3. Run requests sequentially

### Using cURL
See Quick Start Example above or refer to `POSTMAN_EXAMPLES.md` for detailed examples.

### Using React Frontend
Follow the guide in `REACT_INTEGRATION.md` to integrate with React.

## 🔄 Authentication Flow

```
1. User registers or logs in
2. Server validates credentials
3. Server generates JWT tokens (access + refresh)
4. Client stores tokens in localStorage
5. Client includes access token in Authorization header
6. Server validates token for each request
7. If token expires, client uses refresh token to get new access token
8. If refresh token expires, user must log in again
```

## 📊 Database Schema

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
  active BOOLEAN DEFAULT true,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL,
  INDEX idx_username (username),
  INDEX idx_email (email),
  INDEX idx_role (role)
);
```

## 🚨 Error Handling

All errors return consistent JSON format:

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Error description",
  "path": "/api/endpoint"
}
```

HTTP Status Codes:
- `200` - Success
- `201` - Created
- `400` - Bad Request
- `401` - Unauthorized
- `403` - Forbidden
- `404` - Not Found
- `500` - Internal Server Error

## 🔒 Security Best Practices

1. **JWT Secret** - Use strong, randomly generated secret (min 32 characters)
2. **HTTPS** - Use HTTPS in production (configure SSL/TLS)
3. **CORS** - Configure only allowed origins in production
4. **Password Policy** - Enforce strong passwords
5. **Token Expiration** - Keep tokens short-lived (default 24 hours)
6. **Refresh Tokens** - Store securely, use httpOnly cookies in production
7. **Audit Logging** - Monitor all authentication events
8. **Rate Limiting** - Add rate limiting for login attempts
9. **Input Validation** - Always validate user input
10. **Database** - Use parameterized queries (automatic with JPA)

## 📝 Configuration Properties

Key properties in `application.properties`:

```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
spring.jpa.hibernate.ddl-auto=update

# JWT
app.jwt.secret=<your-secret>
app.jwt.expiration=86400000 (24 hours in milliseconds)
app.jwt.refresh-expiration=604800000 (7 days in milliseconds)

# CORS
spring.mvc.cors.allowed-origins=http://localhost:3000
spring.mvc.cors.allowed-methods=GET,POST,PUT,DELETE
```

## 🐛 Troubleshooting

### Database Connection Error
```
Error: Connection refused
Solution: Ensure MySQL is running and credentials are correct
```

### JWT Secret Not Set
```
Error: JWT secret key not configured
Solution: Add app.jwt.secret to application.properties
```

### CORS Error from Frontend
```
Error: CORS policy blocked
Solution: Update allowed-origins in application.properties
```

### Port Already in Use
```
Error: Port 8080 already in use
Solution: Change port in application.properties or kill process using port 8080
```

## 🚀 Deployment

### Docker Deployment
```dockerfile
FROM openjdk:21-jdk-slim
COPY target/hospital-followup-system-1.0.0.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Production Checklist
- [ ] Use strong JWT secret (32+ characters)
- [ ] Enable HTTPS/SSL
- [ ] Configure CORS for production domains
- [ ] Set up MySQL backups
- [ ] Enable database user authentication
- [ ] Change default admin credentials
- [ ] Set environment variables for sensitive data
- [ ] Configure logging to files
- [ ] Set up monitoring and alerting
- [ ] Use reverse proxy (Nginx/Apache)
- [ ] Enable rate limiting
- [ ] Regular security updates

## 📚 Learning Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [JWT Best Practices](https://tools.ietf.org/html/rfc8725)
- [OWASP Security Guidelines](https://owasp.org/)

## 🤝 Contributing

1. Create a feature branch
2. Make your changes
3. Write tests
4. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 👥 Support

For issues, questions, or suggestions:
1. Check the documentation files
2. Review the API examples
3. Check existing GitHub issues
4. Create a new issue with detailed information

## 📞 Contact

For inquiries about this project, please refer to your organization's support channels.

---

**Made with ❤️ using Spring Boot**

Happy coding! 🚀
