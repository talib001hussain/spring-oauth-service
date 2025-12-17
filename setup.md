# Authentication Service Setup Guide

This guide provides instructions on how to set up and run the Authentication Service both locally and with Docker.

## Prerequisites

- Java 21 or later
- Maven 3.6.0 or later
- PostgreSQL 13 or later
- Docker (for Docker setup)

## Local Setup

### 1. Database Setup

1. Install PostgreSQL if you haven't already.
2. You can initialize the database in one of the following ways:

   a. Using the provided PostgreSQL script:
      ```bash
      # Create the database and initialize it with tables and test data
      psql -U postgres -f src/main/resources/db/postgres_init.sql
      ```

   b. Manually create the database and let Spring Boot initialize it:
      ```sql
      CREATE DATABASE authdb;
      ```
      ```sql
      CREATE USER authuser WITH PASSWORD 'password';
      GRANT ALL PRIVILEGES ON DATABASE authdb TO authuser;
      ```
      The application will automatically create the tables and insert test data using the schema.sql and data.sql files.

### 2. Application Configuration

1. Clone the repository:
   ```bash
   git clone https://github.com/talib001hussain/spring-oauth-service
   cd spring-auth-service
   ```

2. Configure the database connection in `src/main/resources/application.yaml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5433/authdb
       username: postgres  # or your custom username
       password: admin  # or your custom password
   ```

3. (Optional) The application uses JWT for API authentication and OAuth2 for web interface authentication. The JWT configuration is handled internally by the JwtTokenUtil class, but you can configure OAuth2 providers in `src/main/resources/application.yaml`:
   ```yaml
   spring:
     security:
       oauth2:
         client:
           registration:
             github:
               client-id: ${OAUTH2_CLIENT_ID:your-client-id}
               client-secret: ${OAUTH2_CLIENT_SECRET:your-client-secret}
               scope: user:email, read:user
             google:
               client-id: ${OAUTH2_GOOGLE_CLIENT_ID:your-google-client-id}
               client-secret: ${OAUTH2_GOOGLE_CLIENT_SECRET:your-google-client-secret}
               scope: email, profile
   ```

### 3. Build and Run

1. Build the application:
   ```bash
   mvn clean install
   ```

2. Run the application:
   ```bash
   mvn spring-boot:run
   ```

3. The application will be available at `http://localhost:8080`

## Docker Setup

### 1. Build Docker Image

1. Build the Docker image:
   ```bash
   docker build -t auth-service .
   ```

### 2. Run with Docker Compose

1. The project includes a `docker-compose.yml` file with the following configuration:
   ```yaml
   version: '3.8'

   services:
     postgres:
       image: postgres:13
       container_name: postgres
       environment:
         POSTGRES_DB: authdb
         POSTGRES_USER: postgres
         POSTGRES_PASSWORD: admin
       ports:
         - "5433:5432"
       volumes:
         - postgres-data:/var/lib/postgresql/data
         - ./src/main/resources/db/docker_init.sql:/docker-entrypoint-initdb.d/init.sql
       healthcheck:
         test: ["CMD-SHELL", "pg_isready -U postgres"]
         interval: 10s
         timeout: 5s
         retries: 5

     auth-service:
       build: .
       container_name: auth-service
       depends_on:
         postgres:
           condition: service_healthy
       environment:
         SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/authdb
         SPRING_DATASOURCE_USERNAME: postgres
         SPRING_DATASOURCE_PASSWORD: admin
         OAUTH2_CLIENT_ID: your-client-id
         OAUTH2_CLIENT_SECRET: your-client-secret
         OAUTH2_GOOGLE_CLIENT_ID: your-google-client-id
         OAUTH2_GOOGLE_CLIENT_SECRET: your-google-client-secret
       ports:
         - "8080:8080"

   volumes:
     postgres-data:
   ```

2. Run with Docker Compose:
   ```bash
   docker-compose up -d
   ```

3. The PostgreSQL container will automatically initialize the database using the script mounted at `/docker-entrypoint-initdb.d/init.sql`. This script creates the necessary tables and inserts test data.

4. The application will be available at `http://localhost:8080`

## API Endpoints

### Authentication

- **Sign Up**: `POST /api/auth/signup`
  ```json
  {
    "email": "user@example.com",
    "password": "password"
  }
  ```

- **Sign In**: `POST /api/auth/signin`
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

### User Profile

- **Get User Profile**: `GET /api/user/profile`
  - Requires JWT token in Authorization header: `Bearer <token>`

- **Update User Profile**: `PUT /api/user/profile`
  - Requires JWT token in Authorization header: `Bearer <token>`
  ```json
  {
    "name": "John Doe",
    "height": 180.0,
    "bmi": 24.5
  }
  ```

## Database Scripts

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

## Troubleshooting

- **Database Connection Issues**: Ensure PostgreSQL is running and the connection details in `application.yaml` are correct.
- **JWT Token Issues**: Check that the JWT secret is properly configured and that the token is being sent in the Authorization header.
- **Docker Issues**: Ensure Docker and Docker Compose are installed and running correctly.
