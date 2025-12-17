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
git clone https://github.com/talib001hussain/spring-oauth-service
cd spring-auth-service
```

### Database Setup

You can either use a local PostgreSQL instance or Docker:

#### Using Local PostgreSQL

1. Install PostgreSQL if you haven't already.
2. Create a database named `authdb` and initialize it with the provided script:
```bash
psql -U postgres -f src/main/resources/db/postgres_init.sql
```

Note: The application is configured to connect to PostgreSQL on port 5433. If your PostgreSQL instance is running on a different port, update the `application.yaml` file accordingly.

#### Using Docker

1. Build the Docker image:
```bash
docker build -t auth-service .
```

2. Run with Docker Compose:
```bash
docker-compose up -d
```

The application will be available at `http://localhost:8080`. The PostgreSQL container will automatically initialize the database using the script mounted at `/docker-entrypoint-initdb.d/init.sql`.

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
  sql:
    init:
      mode: always
      schema-locations: classpath:db/schema.sql
      data-locations: classpath:db/data.sql

server:
  port: 8080
```

The application also supports OAuth2 authentication with GitHub and Google, which can be configured in the same file.

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
  "password": "password"
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
  "password": "password"
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

The application uses Spring Security with both JWT and OAuth2 for authentication:

- JWT is used for API authentication
- OAuth2 is used for web interface authentication with GitHub and Google providers

The security configuration includes:

- CSRF protection disabled for API endpoints
- Public access to authentication endpoints
- JWT-based authentication for API endpoints
- OAuth2-based authentication for web interface
- Stateless session management

### OAuth2 Login

The application provides a web interface for OAuth2 login at `/login`. Users can choose to authenticate with:

- GitHub
- Google

After successful OAuth2 authentication, users will be redirected to their profile page.

## Database

### Schema

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

### Database Scripts

The project includes several SQL scripts for database initialization:

1. **init.sql**: A general initialization script that creates the database, tables, and inserts test data.
   - Location: `src/main/resources/db/init.sql`
   - Usage: Can be executed manually using a database client or command line.

2. **schema.sql**: Contains only the table creation statements.
   - Location: `src/main/resources/db/schema.sql`
   - Usage: Automatically executed by Spring Boot during startup if configured in `application.yaml`.

3. **data.sql**: Contains only the data insertion statements.
   - Location: `src/main/resources/db/data.sql`
   - Usage: Automatically executed by Spring Boot during startup if configured in `application.yaml`.

4. **postgres_init.sql**: A PostgreSQL-specific initialization script.
   - Location: `src/main/resources/db/postgres_init.sql`
   - Usage: Can be executed manually using the psql command line tool.

5. **docker_init.sql**: A Docker-specific initialization script.
   - Location: `src/main/resources/db/docker_init.sql`
   - Usage: Automatically executed when the PostgreSQL container starts in Docker Compose.

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
