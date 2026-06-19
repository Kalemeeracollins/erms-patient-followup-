# Hospital Patient Follow-Up System - Project Structure

## Complete Folder Structure

```
hospital-followup-system/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── backend/
│       │               ├── HospitalFollowUpSystemApplication.java
│       │               ├── auth/
│       │               │   ├── AuthController.java
│       │               │   ├── AuthService.java
│       │               │   └── dto/
│       │               │       ├── LoginRequest.java
│       │               │       ├── LoginResponse.java
│       │               │       └── RegisterRequest.java
│       │               ├── audit/
│       │               │   └── AuditService.java
│       │               ├── common/
│       │               │   └── exception/
│       │               │       ├── BadRequestException.java
│       │               │       ├── GlobalExceptionHandler.java
│       │               │       └── ResourceNotFoundException.java
│       │               ├── security/
│       │               │   ├── CustomUserDetailsService.java
│       │               │   ├── JwtAuthenticationEntryPoint.java
│       │               │   ├── JwtAuthenticationFilter.java
│       │               │   ├── JwtService.java
│       │               │   └── SecurityConfig.java
│       │               ├── user/
│       │               │   ├── Role.java
│       │               │   ├── User.java
│       │               │   ├── UserController.java
│       │               │   ├── UserRepository.java
│       │               │   ├── UserService.java
│       │               │   └── dto/
│       │               │       └── UserDTO.java
│       │               ├── followup/     (Empty - for future implementation)
│       │               ├── notification/ (Empty - for future implementation)
│       │               ├── patient/      (Empty - for future implementation)
│       │               ├── report/       (Empty - for future implementation)
│       │               └── visit/        (Empty - for future implementation)
│       └── resources/
│           └── application.properties
├── pom.xml
├── API_DOCUMENTATION.md
├── POSTMAN_EXAMPLES.md
├── REACT_INTEGRATION.md
└── PROJECT_STRUCTURE.md
```

---

## File Descriptions

### Core Application Files

#### `HospitalFollowUpSystemApplication.java`
- Main Spring Boot application entry point
- Contains main() method to start the application
- Uses `@SpringBootApplication` annotation

**Key Features:**
- Bootstraps the entire Spring Boot application
- Enables auto-configuration of dependencies
- Scans all packages under `com.example.backend` for components

---

### Authentication Module (`auth/`)

#### `AuthController.java`
- REST controller for authentication endpoints
- Handles login, registration, token refresh, and logout
- Uses `@RestController` and `@RequestMapping("/auth")`

**Endpoints:**
- `POST /auth/login` - User login
- `POST /auth/register` - User registration
- `POST /auth/refresh` - Refresh JWT token
- `GET /auth/me` - Get current user info
- `POST /auth/logout` - Logout user

**Features:**
- Input validation using `@Valid` annotation
- CORS configuration for React frontend
- Comprehensive logging

#### `AuthService.java`
- Business logic for authentication operations
- Handles user login, registration, and token management
- Uses `@Service` annotation with constructor injection

**Key Methods:**
- `login(LoginRequest)` - Authenticates user and returns JWT tokens
- `register(RegisterRequest)` - Creates new user account
- `refreshAccessToken(String)` - Generates new access token

**Security Features:**
- Password encoding using BCrypt
- JWT token generation with expiration
- Audit logging of login/registration events

#### `LoginRequest.java`
- DTO for login request
- Contains username and password fields
- Includes validation annotations

#### `LoginResponse.java`
- DTO for login response
- Contains access token, refresh token, and user information
- Nested UserInfo class for user data

#### `RegisterRequest.java`
- DTO for registration request
- Contains user registration fields with validation
- Validates password confirmation and field formats

---

### User Management Module (`user/`)

#### `User.java`
- JPA entity representing a user in the database
- Implements `UserDetails` interface for Spring Security integration
- Uses Lombok annotations for automatic getter/setter generation

**Key Annotations:**
- `@Entity` - Marks as JPA entity
- `@Table` - Specifies database table with indexes
- `@Builder` - Enables builder pattern
- `@PreUpdate` - Automatically updates the `updatedAt` field

**Fields:**
- id, username (unique), password, fullName, email (unique), phoneNumber
- role (enum), active, createdAt, updatedAt

**Key Methods:**
- `getAuthorities()` - Returns user's roles/authorities
- `isEnabled()` - Checks if user is active

#### `Role.java`
- Enum defining available user roles
- Contains: ADMIN, DOCTOR, NURSE, RECEPTIONIST
- Each role has code and description

#### `UserRepository.java`
- Spring Data JPA repository interface
- Provides database access methods
- Extends `JpaRepository<User, Long>`

**Key Methods:**
- `findByUsername(String)` - Find user by username
- `findByEmail(String)` - Find user by email
- `existsByUsername(String)` - Check if username exists
- `existsByEmail(String)` - Check if email exists

#### `UserService.java`
- Business logic for user management
- Uses `@Service` annotation with transactional support
- Constructor injection of dependencies

**Key Methods:**
- `getAllUsers()` - Retrieve all users with DTO conversion
- `getUserById(Long)` - Get user by ID
- `updateUser(Long, String, String, String)` - Update user information
- `setUserActive(Long, boolean)` - Activate or deactivate user

**Features:**
- DTO conversion for API responses
- Duplicate email validation
- Audit logging of user changes

#### `UserController.java`
- REST controller for user management endpoints
- Restricted to admin users with `@PreAuthorize` annotations
- Uses `@RestController` and `@RequestMapping("/users")`

**Endpoints:**
- `GET /users` - Get all users (Admin only)
- `GET /users/{id}` - Get user by ID
- `PUT /users/{id}` - Update user
- `PUT /users/{id}/activate` - Activate user
- `PUT /users/{id}/deactivate` - Deactivate user
- `DELETE /users/{id}` - Delete user

#### `UserDTO.java`
- Data Transfer Object for user information
- Used in API responses to avoid exposing entity details
- Contains non-sensitive user information

---

### Security Module (`security/`)

#### `JwtService.java`
- Service for JWT token generation and validation
- Uses JJWT library for token operations
- Property-based configuration for secret and expiration

**Key Methods:**
- `generateToken(UserDetails)` - Generate access token
- `generateRefreshToken(UserDetails)` - Generate refresh token
- `isTokenValid(String, UserDetails)` - Validate token
- `extractUsername(String)` - Extract username from token
- `extractAllClaims(String)` - Parse and return all claims

**Features:**
- HS256 signing algorithm
- Configurable token expiration (default: 24 hours)
- Refresh token with longer expiration (default: 7 days)
- Comprehensive error handling for JWT exceptions

#### `JwtAuthenticationFilter.java`
- Filter that intercepts all HTTP requests
- Extracts JWT from Authorization header
- Validates token and sets security context
- Extends `OncePerRequestFilter` for single execution

**Key Features:**
- Extracts "Bearer <token>" from Authorization header
- Validates token for each request
- Sets authentication in SecurityContextHolder if valid
- Logs authentication events

#### `CustomUserDetailsService.java`
- Implements Spring Security's `UserDetailsService`
- Loads user details from database by username
- Used by authentication manager

**Key Method:**
- `loadUserByUsername(String)` - Loads user from database

**Features:**
- Checks if user account is active
- Throws `UsernameNotFoundException` if user not found

#### `JwtAuthenticationEntryPoint.java`
- Handles unauthorized access attempts
- Returns JSON error response instead of HTML
- Implements `AuthenticationEntryPoint`

**Features:**
- Returns 401 status with JSON error message
- Includes error details, timestamp, and request path

#### `SecurityConfig.java`
- Central Spring Security configuration
- Uses `@Configuration` and `@EnableWebSecurity` annotations
- Configures authentication, authorization, CORS, and filters

**Key Beans:**
- `passwordEncoder()` - BCrypt password encoder
- `authenticationProvider()` - DAO authentication provider
- `authenticationManager()` - Authentication manager
- `corsConfigurationSource()` - CORS configuration
- `filterChain(HttpSecurity)` - Main security filter chain

**Security Configuration:**
- CORS enabled for localhost:3000 and localhost:5173
- CSRF disabled (JWT is stateless)
- Stateless session management
- JWT filter added before UsernamePasswordAuthenticationFilter
- Public endpoints: /auth/login, /auth/register, /auth/refresh
- Protected endpoints require authentication
- Admin-only endpoints for user management

---

### Exception Handling Module (`common/exception/`)

#### `ResourceNotFoundException.java`
- Custom exception for missing resources
- Extends `RuntimeException`
- Thrown when entity is not found

#### `BadRequestException.java`
- Custom exception for invalid requests
- Extends `RuntimeException`
- Thrown for business logic violations

#### `GlobalExceptionHandler.java`
- Central exception handler for all controllers
- Uses `@RestControllerAdvice` annotation
- Provides consistent error response format

**Key Handlers:**
- `handleResourceNotFoundException()` - Returns 404
- `handleBadRequestException()` - Returns 400
- `handleMethodArgumentNotValid()` - Returns validation errors
- `handleGlobalException()` - Catches all unhandled exceptions

**Features:**
- Consistent JSON error format
- Includes timestamp, status, error, message, path
- Collects all validation errors in one response

---

### Audit Module (`audit/`)

#### `AuditService.java`
- Logs important application events
- Uses SLF4J with logging framework
- Provides audit trail for security and compliance

**Key Methods:**
- `logLoginSuccess()` - Log successful login
- `logLoginFailure()` - Log failed login attempt
- `logUserCreation()` - Log new user registration
- `logUserUpdate()` - Log user information changes
- `logPasswordChange()` - Log password changes
- `logUnauthorizedAccess()` - Log unauthorized access attempts

**Features:**
- All events prefixed with "AUDIT" for easy filtering
- Includes relevant user and event details
- Uses appropriate log levels (info, warn, error)

---

### Configuration Files

#### `application.properties`
- Spring Boot application configuration
- Contains database, JWT, CORS, and logging settings

**Key Configurations:**
```properties
# Server
server.port=8080
server.servlet.context-path=/api

# Database (MySQL)
spring.datasource.url=jdbc:mysql://localhost:3306/hospital_db
spring.jpa.hibernate.ddl-auto=update

# JWT
app.jwt.secret=<your-secret-key>
app.jwt.expiration=86400000 (24 hours)
app.jwt.refresh-expiration=604800000 (7 days)

# CORS
spring.mvc.cors.allowed-origins=http://localhost:3000
spring.mvc.cors.allowed-methods=GET,POST,PUT,DELETE
```

#### `pom.xml`
- Maven build configuration
- Defines all project dependencies
- Specifies Java 21 as target version

**Key Dependencies:**
- Spring Boot Web, Security, Data JPA
- JWT (JJWT)
- MySQL Driver
- Lombok
- Validation

---

## Feature-Based Architecture Benefits

This architecture provides several advantages:

1. **Scalability** - Easy to add new features (followup, patient, visit modules)
2. **Maintainability** - Related code is grouped together
3. **Testing** - Each module can be tested independently
4. **Security** - Centralized security configuration
5. **Clarity** - Clear separation of concerns

---

## Database Generation

Hibernate automatically creates tables based on entity definitions:

- Table names are derived from entity class names
- Column names from entity field names
- Indexes created as specified in `@Index` annotations
- Constraints (unique, not null) from field annotations

**Important:** Set `spring.jpa.hibernate.ddl-auto=validate` in production to prevent accidental schema changes.

---

## Running the Application

### Prerequisites
- Java 21 installed
- MySQL 8 server running
- Maven installed

### Steps
```bash
# 1. Create database
CREATE DATABASE hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

# 2. Clone/download project
cd hospital-followup-system

# 3. Build project
mvn clean install

# 4. Run application
mvn spring-boot:run

# Application will be available at: http://localhost:8080/api
```

---

## Future Implementation

The following modules are scaffolded and ready for implementation:

- **Patient Module** - Patient information management
- **Visit Module** - Medical visit records
- **Follow-up Module** - Patient follow-up scheduling
- **Notification Module** - Email/SMS notifications
- **Report Module** - Medical reports and analytics

---

## Security Best Practices Implemented

1. ✅ JWT-based stateless authentication
2. ✅ BCrypt password hashing
3. ✅ Role-based access control (RBAC)
4. ✅ CORS configuration for specific origins
5. ✅ CSRF protection disabled (appropriate for stateless API)
6. ✅ Input validation on all endpoints
7. ✅ Comprehensive error handling
8. ✅ Audit logging of important events
9. ✅ SQL injection prevention (using JPA)
10. ✅ Password confirmation validation
