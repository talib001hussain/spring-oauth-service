# Spring Boot Authentication Service

A robust authentication service built with Spring Boot that provides JWT-based authentication and user profile management.

## Features

- User registration and authentication
- JWT token-based security
- User profile management
- RESTful API design
- PostgreSQL database integration

## Technologies Used

- Java 21
- Spring Boot 2.7.18
- Spring Security
- Spring Data JPA
- JWT (JSON Web Tokens)
- PostgreSQL
- Maven
- Docker (for containerization)

## Prerequisites

- JDK 21 or later
- Maven 3.6+
- PostgreSQL 13+ or Docker
- Git

## Getting Started

### Clone the Repository

```bash
git clone https://github.com/yourusername/spring-auth-service.git
cd spring-auth-service
```

### Database Setup

You can either use a local PostgreSQL instance or Docker:

#### Using Local PostgreSQL

1. Create a database named `authdb`
2. Run the initialization script:
```bash
psql -U postgres -f src/main/resources/db/postgres_init.sql
```

#### Using Docker

```bash
docker-compose up -d
```

### Configuration

The application can be configured through the `application.yaml` file:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5433/authdb
    username: postgres
    password: admin
  jpa:
    hibernate:
      ddl-auto: none

jwt:
  secret: your-secret-key-should-be-at-least-256-bits
  expiration: 86400000  # 24 hours in milliseconds
```

### Building and Running the Application

```bash
mvn clean install
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`.

## API Documentation

### Authentication Endpoints

#### Sign Up

```
POST /api/auth/signup
```

Request body:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

#### Sign In

```
POST /api/auth/signin
```

Request body:
```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

Response:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "email": "user@example.com"
}
```

### User Profile Endpoints

#### Get User Profile

```
GET /api/user/profile
```

Headers:
```
Authorization: Bearer YOUR_JWT_TOKEN
```

#### Update User Profile

```
PUT /api/user/profile
```

Headers:
```
Authorization: Bearer YOUR_JWT_TOKEN
```

Request body:
```json
{
  "name": "John Doe",
  "height": 180.0,
  "bmi": 24.5
}
```

## Testing with cURL

See the [curl-commands.md](curl-commands.md) file for detailed examples of how to test the API using cURL.

## Security

The application uses Spring Security with JWT for authentication. The security configuration includes:

- CSRF protection disabled for API endpoints
- Public access to authentication endpoints
- JWT-based authentication for all other endpoints
- Stateless session management

## Database Schema

The application uses two main tables:

1. `users` - Stores user authentication information
   - `id` (UUID, primary key)
   - `email` (unique)
   - `password` (BCrypt encrypted)

2. `user_profiles` - Stores user profile information
   - `user_id` (UUID, foreign key to users.id)
   - `full_name`
   - `height`
   - `bmi`

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.